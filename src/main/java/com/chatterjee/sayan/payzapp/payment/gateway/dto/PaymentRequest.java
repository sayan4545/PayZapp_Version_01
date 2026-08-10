package com.chatterjee.sayan.payzapp.payment.gateway.dto;

import com.chatterjee.sayan.payzapp.common.entities.Money;
import com.chatterjee.sayan.payzapp.common.enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;

public record PaymentRequest(

        UUID paymentId,
        UUID merchantId,
        UUID orderId,
        Money amount,
        PaymentMethod paymentMethod,
        Map<String,Object> methodDetails


) {
}
