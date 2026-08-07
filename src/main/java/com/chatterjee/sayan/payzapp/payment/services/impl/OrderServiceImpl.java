package com.chatterjee.sayan.payzapp.payment.services.impl;

import com.chatterjee.sayan.payzapp.common.enums.OrderStatus;
import com.chatterjee.sayan.payzapp.common.exceptions.BusinessRuleViolationException;
import com.chatterjee.sayan.payzapp.common.exceptions.DuplicateResourceException;
import com.chatterjee.sayan.payzapp.common.exceptions.ResourceNotFoundException;
import com.chatterjee.sayan.payzapp.payment.dto.request.CreateOrderRequest;
import com.chatterjee.sayan.payzapp.payment.dto.response.OrderResponse;
import com.chatterjee.sayan.payzapp.payment.dto.response.PaymentResponse;
import com.chatterjee.sayan.payzapp.payment.entities.OrderRecord;
import com.chatterjee.sayan.payzapp.payment.entities.Payment;
import com.chatterjee.sayan.payzapp.payment.mapper.OrderMapper;
import com.chatterjee.sayan.payzapp.payment.mapper.PaymentMapper;
import com.chatterjee.sayan.payzapp.payment.repositories.OrderRepository;
import com.chatterjee.sayan.payzapp.payment.repositories.PaymentRepository;
import com.chatterjee.sayan.payzapp.payment.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository  paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;

    @Value("${payment_default_timeout_in_mins : 30}")
    private int defaultExpiryMins;
    @Override
    public OrderResponse createOrder(UUID merchantId, CreateOrderRequest request) {
        // check if the reciept is not duplicate
        if(request.receipt()!=null && orderRepository.existsByMerchantIdAndReceipt(merchantId,request.receipt())){
            throw new DuplicateResourceException("ORDER_RECEIPT_DUPLICATE","Order with the order_id exists"+ request.receipt());
        }

        OrderRecord order = OrderRecord
                .builder()
                .receipt(request.receipt())
                .amount(request.amount())
                .orderStatus(OrderStatus.CREATED)
                .merchantId(merchantId)
                .notes(request.notes())
                .expiresAt(request.expiresAt()!=null ? request.expiresAt() : LocalDateTime.now().plusMinutes(defaultExpiryMins))
        .build();

        orderRepository.save(order);

        return orderMapper.toOrderResponse(order);

//        return new OrderResponse(order.getId(),
//                merchantId,
//                order.getReceipt(),
//                order.getAmount(),
//                order.getOrderStatus(),
//                order.getAttempts(),
//                order.getNotes(),
//                order.getExpiresAt(),
//                null);




    }

    @Override
    public OrderResponse getOrderById(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository
                .findByMerchantIdAndId(merchantId,orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order",orderId));

        return new OrderResponse(order.getId(),order.getMerchantId()
        ,order.getReceipt(),order.getAmount(),order.getOrderStatus()
                ,order.getAttempts(),order.getNotes(),order.getExpiresAt(),null);
    }

    @Override
    public OrderResponse cancel(UUID merchantId, UUID orderId) {

        // get the order
        OrderRecord order =
                orderRepository.findByMerchantIdAndId(merchantId,orderId)
                        .orElseThrow(()-> new ResourceNotFoundException("Order",orderId));


        if(order.getOrderStatus().equals(OrderStatus.CANCELLED)|| order.getOrderStatus().equals(OrderStatus.PAID)){
            throw new BusinessRuleViolationException("Order already paid or cancelled"+order.getOrderStatus().name(),"ORDER_CANNOT_BE_CANCELLED");
        }
        // change order status to cancel

        order.setOrderStatus(OrderStatus.CANCELLED);
        // persist in the repository
        orderRepository.save(order);

        // return the orderResponse object
        // TODO : use mapstruct // done
//        return new OrderResponse(order.getId()
//                ,order.getMerchantId(),
//                order.getReceipt(),
//                order.getAmount(),
//                order.getOrderStatus(),
//                order.getAttempts(),
//                order.getNotes(),
//                order.getExpiresAt(),
//                null ); // TODO : to be populated while auditing)
        return  orderMapper.toOrderResponse(order);



    }

    @Override
    public List<PaymentResponse> listPayments(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByMerchantIdAndId(merchantId,orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order",orderId));

        List<Payment> paymentList = paymentRepository.findByOrder_Id(order);

        // convert the list of payments to paymentResponse
        // 1. use normal contructor

//        return paymentList
//                .stream()
//                .map(payment -> new PaymentResponse(payment.getId(),orderId,payment.getMerchantId()
//                ,payment.getMoney(),payment.getPaymentStatus(),payment.getPaymentMethod(),payment.getMethodDetails(),null,null,payment.getBankReference(),payment.getErrorCode(),payment.getErrorDescription(),null,payment.getCapturedAt(),null))
//                .toList();


        //2 .  TODO :use mapstruct  // done


//        return paymentList
//                .stream()
//                .map(paymentMapper::toResponse)
//                .collect(Collectors.toList());

        return paymentMapper.toResponseList(paymentList);



    }
}
