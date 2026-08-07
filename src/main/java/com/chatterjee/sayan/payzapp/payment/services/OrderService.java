package com.chatterjee.sayan.payzapp.payment.services;

import com.chatterjee.sayan.payzapp.payment.dto.request.CreateOrderRequest;
import com.chatterjee.sayan.payzapp.payment.dto.response.OrderResponse;
import com.chatterjee.sayan.payzapp.payment.dto.response.PaymentResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponse createOrder(UUID merchantId, CreateOrderRequest request);

    OrderResponse getOrderById(UUID merchantId, UUID orderId);

    OrderResponse cancel(UUID merchantId, UUID orderId);

    List<PaymentResponse> listPayments(UUID merchantId, UUID orderId);
}
