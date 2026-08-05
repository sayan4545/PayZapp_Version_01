package com.chatterjee.sayan.payzapp.payment.services;

import com.chatterjee.sayan.payzapp.payment.dto.request.CreateOrderRequest;
import com.chatterjee.sayan.payzapp.payment.dto.response.OrderResponse;

import java.util.UUID;

public interface OrderService {

    OrderResponse createOrder(UUID merchantId, CreateOrderRequest request);
}
