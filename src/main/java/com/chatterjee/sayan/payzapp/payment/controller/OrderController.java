package com.chatterjee.sayan.payzapp.payment.controller;

import com.chatterjee.sayan.payzapp.merchant.security.MerchantContext;
import com.chatterjee.sayan.payzapp.payment.dto.request.CreateOrderRequest;
import com.chatterjee.sayan.payzapp.payment.dto.response.OrderResponse;
import com.chatterjee.sayan.payzapp.payment.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    //UUID merchantId = UUID.fromString("0c8f873c-9018-4990-bd1f-f9dea20cff9d");
    private final MerchantContext merchantContext;


    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request){

        return ResponseEntity
                .status(HttpStatus.CREATED).body(orderService.createOrder(merchantContext.getMerchantId(),request));

    }
}
