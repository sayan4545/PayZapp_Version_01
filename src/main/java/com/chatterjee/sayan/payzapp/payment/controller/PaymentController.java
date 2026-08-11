package com.chatterjee.sayan.payzapp.payment.controller;

import com.chatterjee.sayan.payzapp.payment.dto.request.InitPaymentRequest;
import com.chatterjee.sayan.payzapp.payment.dto.response.PaymentResponse;
import com.chatterjee.sayan.payzapp.payment.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    UUID merchantId = UUID.fromString("00000000-0000-0000-0000-0000000000");// TODO: use security

    @PostMapping
    public ResponseEntity<PaymentResponse> initiatePayment(@Valid @RequestBody InitPaymentRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.initiatePayment(merchantId, request));
    }
    @PostMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> capture(@PathVariable UUID paymentId){
        return ResponseEntity.ok(paymentService.capture(merchantId,paymentId));
    }


}
