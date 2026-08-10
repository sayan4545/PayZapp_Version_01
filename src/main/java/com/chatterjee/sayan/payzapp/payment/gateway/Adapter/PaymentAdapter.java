package com.chatterjee.sayan.payzapp.payment.gateway.Adapter;

import com.chatterjee.sayan.payzapp.payment.gateway.dto.PaymentRequest;
import com.chatterjee.sayan.payzapp.payment.gateway.dto.PaymentResult;

public interface PaymentAdapter {

    public PaymentResult initiate(PaymentRequest paymentRequest);
}
