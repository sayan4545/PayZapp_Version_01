package com.chatterjee.sayan.payzapp.payment.controller;

import com.chatterjee.sayan.payzapp.merchant.security.MerchantContext;
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
    //UUID merchantId = UUID.fromString("0c8f873c-9018-4990-bd1f-f9dea20cff9d");
    private final MerchantContext merchantContext;

    @PostMapping
    public ResponseEntity<PaymentResponse> initiatePayment(@Valid @RequestBody InitPaymentRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.initiatePayment(merchantContext.getMerchantId(), request));
    }
    @PostMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> capture(@PathVariable UUID paymentId){
        return ResponseEntity.ok(paymentService.capture(merchantContext.getMerchantId(),paymentId));
    }


}
