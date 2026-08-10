package com.chatterjee.sayan.payzapp.payment.processor;

import com.chatterjee.sayan.payzapp.common.entities.Money;
import com.chatterjee.sayan.payzapp.payment.processor.dto.PaymentProcessorRequest;
import com.chatterjee.sayan.payzapp.payment.processor.dto.PaymentProcessorResponse;

public interface PaymentProcessor {

    PaymentProcessorResponse charge (PaymentProcessorRequest request);



}
