package com.chatterjee.sayan.payzapp.payment.gateway.dto;

public sealed interface PaymentResult permits
        PaymentResult.Pending,
        PaymentResult.Failure,
        PaymentResult.Success
{

    record Pending(String registrationDetails) implements PaymentResult {}
    record Failure(String errorCode, String errorDetails) implements PaymentResult {}
    record Success(String bankReference) implements PaymentResult {};
}
