package com.chatterjee.sayan.payzapp.payment.processor;

import com.chatterjee.sayan.payzapp.common.enums.PaymentMethod;
import com.chatterjee.sayan.payzapp.payment.processor.dto.PaymentProcessorRequest;
import com.chatterjee.sayan.payzapp.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentProcessorRouter {

    private final Map<PaymentMethod, PaymentProcessor> paymentProcessorMap;

    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        PaymentProcessor processor = paymentProcessorMap.get(request.paymentMethod());
        if (processor == null) {
            throw new IllegalArgumentException("PaymentProcessor not found for payment method: " + request.paymentMethod());

        }
        return processor.charge(request);
    }
}
