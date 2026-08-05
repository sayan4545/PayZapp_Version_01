package com.chatterjee.sayan.payzapp.payment.dto.response;

import com.chatterjee.sayan.payzapp.common.entities.Money;
import com.chatterjee.sayan.payzapp.common.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record OrderResponse(

        UUID id,
        UUID merchantId,
        String receipt,
        Money amount,
        OrderStatus orderStatus,
        Integer attempts,
        Map<String,Object> notes,
        LocalDateTime expiresAt,
        LocalDateTime createdAt
) {
}
