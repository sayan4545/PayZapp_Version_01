package com.chatterjee.sayan.payzapp.payment.processor.dto;

public sealed interface PaymentProcessorResponse permits
            PaymentProcessorResponse.Pending,PaymentProcessorResponse.Success,
            PaymentProcessorResponse.Failed{

    record Pending(String processorReference) implements PaymentProcessorResponse {}
    record Success(String processorReference,String bankReference) implements PaymentProcessorResponse {}
    record Failed(String errorCode, String errorDescription) implements PaymentProcessorResponse {}
}
