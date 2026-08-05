package com.chatterjee.sayan.payzapp.payment.services.impl;

import com.chatterjee.sayan.payzapp.common.enums.OrderStatus;
import com.chatterjee.sayan.payzapp.common.exceptions.DuplicateResourceException;
import com.chatterjee.sayan.payzapp.payment.dto.request.CreateOrderRequest;
import com.chatterjee.sayan.payzapp.payment.dto.response.OrderResponse;
import com.chatterjee.sayan.payzapp.payment.entities.OrderRecord;
import com.chatterjee.sayan.payzapp.payment.repositories.OrderRepository;
import com.chatterjee.sayan.payzapp.payment.repositories.PaymentRepository;
import com.chatterjee.sayan.payzapp.payment.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository  paymentRepository;

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

        return new OrderResponse(order.getId(),
                merchantId,
                order.getReceipt(),
                order.getAmount(),
                order.getOrderStatus(),
                order.getAttempts(),
                order.getNotes(),
                order.getExpiresAt(),
                null);




    }
}
