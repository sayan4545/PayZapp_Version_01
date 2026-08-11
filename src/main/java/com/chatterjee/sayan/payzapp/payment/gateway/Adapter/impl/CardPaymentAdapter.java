package com.chatterjee.sayan.payzapp.payment.gateway.Adapter.impl;

import com.chatterjee.sayan.payzapp.payment.gateway.Adapter.PaymentAdapter;
import com.chatterjee.sayan.payzapp.payment.gateway.dto.PaymentRequest;
import com.chatterjee.sayan.payzapp.payment.gateway.dto.PaymentResult;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CardPaymentAdapter implements PaymentAdapter {
    @Override
    public PaymentResult initiate(PaymentRequest paymentRequest) {
        return null;
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return null;
    }
}
