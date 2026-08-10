package com.chatterjee.sayan.payzapp.payment.processor.strategy;

import com.chatterjee.sayan.payzapp.payment.processor.PaymentProcessor;
import com.chatterjee.sayan.payzapp.payment.processor.dto.PaymentProcessorRequest;
import com.chatterjee.sayan.payzapp.payment.processor.dto.PaymentProcessorResponse;

public class UPIPaymentsProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        return null;
    }
}
