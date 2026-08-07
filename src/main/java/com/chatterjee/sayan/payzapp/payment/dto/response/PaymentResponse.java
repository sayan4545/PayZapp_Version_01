package com.chatterjee.sayan.payzapp.payment.dto.response;

import com.chatterjee.sayan.payzapp.common.entities.Money;
import com.chatterjee.sayan.payzapp.common.enums.PaymentMethod;
import com.chatterjee.sayan.payzapp.common.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentResponse(
        UUID id,
        UUID orderId,
        UUID merchantId,
        Money amount,
        PaymentStatus paymentStatus,
        PaymentMethod paymentMethod,
        Map<String,Object> methodDetails,
        String cardLastFour,
        String cardBank,
        String bankReference,
        String errorCode,
        String errorDescription,
        Long refundedAmountInPaisa,
        LocalDateTime capturedAt,
        LocalDateTime createdAt

) {
}
