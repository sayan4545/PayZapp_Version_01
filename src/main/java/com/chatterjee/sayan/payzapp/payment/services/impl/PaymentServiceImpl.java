package com.chatterjee.sayan.payzapp.payment.services.impl;

import com.chatterjee.sayan.payzapp.common.enums.OrderStatus;
import com.chatterjee.sayan.payzapp.common.enums.PaymentStatus;
import com.chatterjee.sayan.payzapp.common.exceptions.BusinessRuleViolationException;
import com.chatterjee.sayan.payzapp.common.exceptions.ResourceNotFoundException;
import com.chatterjee.sayan.payzapp.payment.dto.request.InitPaymentRequest;
import com.chatterjee.sayan.payzapp.payment.dto.response.PaymentResponse;
import com.chatterjee.sayan.payzapp.payment.entities.OrderRecord;
import com.chatterjee.sayan.payzapp.payment.entities.Payment;
import com.chatterjee.sayan.payzapp.payment.gateway.PaymentGatewayRouter;
import com.chatterjee.sayan.payzapp.payment.gateway.dto.PaymentRequest;
import com.chatterjee.sayan.payzapp.payment.gateway.dto.PaymentResult;
import com.chatterjee.sayan.payzapp.payment.mapper.PaymentMapper;
import com.chatterjee.sayan.payzapp.payment.repositories.OrderRepository;
import com.chatterjee.sayan.payzapp.payment.repositories.PaymentRepository;
import com.chatterjee.sayan.payzapp.payment.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        PaymentResult result = router.initiate(inComingPaymentRequest);

        switch (result) {
            case PaymentResult.Pending pending-> createdPayment.setProcessorReference(pending.registrationDetails());
            case PaymentResult.Failure failure-> {
                createdPayment.setPaymentStatus(PaymentStatus.FAILED);
                createdPayment.setErrorCode(failure.errorCode());
                createdPayment.setErrorDescription(failure.errorDetails());
            }
        }

        // persist the createdPayment again
        Payment savedPayment = paymentRepository.save(createdPayment);
        orderRepository.save(order);

        return paymentMapper.toResponse(savedPayment);


    }
}
