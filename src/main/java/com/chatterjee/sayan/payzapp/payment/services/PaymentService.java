package com.chatterjee.sayan.payzapp.payment.services;

import com.chatterjee.sayan.payzapp.payment.dto.request.InitPaymentRequest;
import com.chatterjee.sayan.payzapp.payment.dto.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse initiatePayment(UUID merchantId, InitPaymentRequest paymentRequest);

    PaymentResponse capture(UUID merchantId, UUID paymentId);
}
