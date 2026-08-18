package com.chatterjee.sayan.payzapp.payment.services.impl;

import com.chatterjee.sayan.payzapp.common.enums.OrderStatus;
import com.chatterjee.sayan.payzapp.common.enums.PaymentEvent;
import com.chatterjee.sayan.payzapp.common.enums.PaymentStatus;
import com.chatterjee.sayan.payzapp.common.exceptions.BusinessRuleViolationException;
import com.chatterjee.sayan.payzapp.common.exceptions.ResourceNotFoundException;
import com.chatterjee.sayan.payzapp.payment.dto.request.InitPaymentRequest;
import com.chatterjee.sayan.payzapp.payment.dto.response.PaymentResponse;
import com.chatterjee.sayan.payzapp.payment.entities.OrderRecord;
import com.chatterjee.sayan.payzapp.payment.entities.Payment;
import com.chatterjee.sayan.payzapp.payment.gateway.Adapter.PaymentAdapter;
import com.chatterjee.sayan.payzapp.payment.gateway.PaymentGatewayRouter;
import com.chatterjee.sayan.payzapp.payment.gateway.dto.PaymentRequest;
import com.chatterjee.sayan.payzapp.payment.gateway.dto.PaymentResult;
import com.chatterjee.sayan.payzapp.payment.mapper.PaymentMapper;
import com.chatterjee.sayan.payzapp.payment.repositories.OrderRepository;
import com.chatterjee.sayan.payzapp.payment.repositories.PaymentRepository;
import com.chatterjee.sayan.payzapp.payment.services.PaymentService;
import com.chatterjee.sayan.payzapp.payment.statemachine.services.PaymentTransitionLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentGatewayRouter router;
    private final PaymentMapper paymentMapper;
    private final PaymentTransitionLogService  paymentTransitionLogService;
    //private final PaymentAdapter paymentAdapter;


    @Override
    @Transactional
    public PaymentResponse initiatePayment(UUID merchantId, InitPaymentRequest paymentRequest) {
        OrderRecord order = orderRepository.findByMerchantIdAndId(merchantId,paymentRequest.orderId())
                .orElseThrow(()-> new ResourceNotFoundException("Order",paymentRequest.orderId()));
        // Check if the order is in CREATED or ATTEMPTED state. Payment can be initiated only for these two state

        if(order.getOrderStatus()== OrderStatus.PAID || order.getOrderStatus()== OrderStatus.CANCELLED){
            throw new BusinessRuleViolationException("ORDER NOT PAYABLE","Order is not paid or CANCELLED");
        }

        order.setOrderStatus(OrderStatus.ATTEMPTED);
        order.setAttempts(order.getAttempts()+1);

        // create the payment object
        Payment toInitiatePayment = Payment.builder()
                .order(order)
                .merchantId(merchantId)
                .amount(order.getAmount())
                .paymentStatus(PaymentStatus.CREATED)
                .paymentMethod(paymentRequest.paymentMethod())
                .methodDetails(paymentRequest.methodDetails())
                .build();

        // persist the payment object
        Payment createdPayment = paymentRepository.save(toInitiatePayment);
        // create a PaymentRequest object

        PaymentRequest inComingPaymentRequest = new PaymentRequest(
                createdPayment.getId(),
                merchantId,
                paymentRequest.orderId(),
                order.getAmount(),
                paymentRequest.paymentMethod(),
                paymentRequest.methodDetails());

        paymentTransitionLogService.apply(createdPayment,PaymentEvent.AUTHORIZE_ATTEMPT);
        PaymentResult result = router.initiate(inComingPaymentRequest);

        switch (result) {
            case PaymentResult.Pending pending-> createdPayment.setProcessorReference(pending.registrationDetails());
            case PaymentResult.Failure failure-> {
               // createdPayment.setPaymentStatus(PaymentStatus.FAILED);
                paymentTransitionLogService.apply(createdPayment, PaymentEvent.AUTHORIZE_FAILURE);
                createdPayment.setErrorCode(failure.errorCode());
                createdPayment.setErrorDescription(failure.errorDetails());
            }
            case PaymentResult.Success success -> {
                log.warn("Invalid state");
                return null;
            }
        }

        // persist the createdPayment again
        Payment savedPayment = paymentRepository.save(createdPayment);
        orderRepository.save(order);

        return paymentMapper.toResponse(savedPayment);


    }

    @Override
    @Transactional
    public PaymentResponse capture(UUID merchantId, UUID paymentId) {
        // validation if the payment is real or not
        Payment payment = paymentRepository.findByIdAndMerchantId(merchantId,paymentId)
                .orElseThrow(()-> new ResourceNotFoundException("Payment",paymentId));

        /*
        mark the payment status as CAPTURING
        Call the paymentGatewayRouter
         */

        //payment.setPaymentStatus(PaymentStatus.CAPTURING);// TODO : do a statemachine
        paymentTransitionLogService.apply(payment,PaymentEvent.CAPTURE_REQUEST);
        PaymentResult paymentResult = router.capture(payment.getPaymentMethod(),paymentId);

        if(paymentResult instanceof PaymentResult.Success success) {

            //payment.setPaymentStatus(PaymentStatus.CAPTURED);
            paymentTransitionLogService.apply(payment,PaymentEvent.CAPTURE_SUCCESS);
            payment.setCapturedAt(LocalDateTime.now());
            log.info("Payment captured successfully,paymentID: {}",paymentId);

        }else if(paymentResult instanceof PaymentResult.Failure failure) {
            //payment.setPaymentStatus(PaymentStatus.AUTHORIZED);
            paymentTransitionLogService.apply(payment,PaymentEvent.CAPTURE_FAILURE);
            payment.setErrorCode(failure.errorCode());
            payment.setErrorDescription(failure.errorDetails());
            log.warn("payment capture failed, paymentID: {}",paymentId);
        }
        // persist the payment object to the db
        paymentRepository.save(payment);
        return paymentMapper.toResponse(payment);
    }

    @Override
    public void resolveAuthorization(UUID paymentId, boolean approve, String bankRef, String errorCode, String errorDescription) {
        // validation if the payment actually exists
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(()-> new ResourceNotFoundException("Payment",paymentId));
        if(payment.getPaymentStatus()!=PaymentStatus.AUTHORIZING){
            log.warn("Payment status is not in authorizing state,paymentId:{},paymentStatus: {}",paymentId,payment.getPaymentStatus());
            return;
        }

        OrderRecord orderRecord = payment.getOrder();
        if(approve){
            paymentTransitionLogService.apply(payment,PaymentEvent.AUTHORIZE_SUCCESS);
            payment.setBankReference(bankRef);
            payment.setAuthorizedAt(LocalDateTime.now());
            // auto-capture

            paymentTransitionLogService.apply(payment,PaymentEvent.CAPTURE_REQUEST);
            PaymentResult captureResult = router.capture(payment.getPaymentMethod(),paymentId);
            if(captureResult instanceof PaymentResult.Success success) {
                paymentTransitionLogService.apply(payment,PaymentEvent.CAPTURE_SUCCESS);
                payment.setCapturedAt(LocalDateTime.now());
                orderRecord.setOrderStatus(OrderStatus.PAID);
            }
            else if(captureResult instanceof PaymentResult.Failure failure) {
                paymentTransitionLogService.apply(payment,PaymentEvent.CAPTURE_FAILURE);
                payment.setErrorCode(failure.errorCode());
                payment.setErrorDescription(failure.errorDetails());

            }
        }
        else {
            paymentTransitionLogService.apply(payment,PaymentEvent.AUTHORIZE_FAILURE);
            payment.setErrorCode(errorCode);
            payment.setErrorDescription(errorDescription);
        }

        paymentRepository.save(payment);
        orderRepository.save(orderRecord);

    }
}
