package com.chatterjee.sayan.payzapp.payment.gateway.Adapter;

import com.chatterjee.sayan.payzapp.payment.gateway.dto.PaymentRequest;
import com.chatterjee.sayan.payzapp.payment.gateway.dto.PaymentResult;

import java.util.UUID;

public interface PaymentAdapter {

    public PaymentResult initiate(PaymentRequest paymentRequest);


    PaymentResult capture(UUID paymentId);
}
