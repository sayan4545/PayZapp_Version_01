package com.chatterjee.sayan.payzapp.payment.dto.request;

import com.chatterjee.sayan.payzapp.common.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record InitPaymentRequest(

        @NotNull(message = "orderId cannot be null")
        UUID orderId,

        @NotNull(message = "payment method cannot be null")
        PaymentMethod paymentMethod,

        Map<String,Object> methodDetails
) {
}
