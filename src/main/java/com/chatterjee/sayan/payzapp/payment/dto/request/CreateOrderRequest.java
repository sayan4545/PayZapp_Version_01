package com.chatterjee.sayan.payzapp.payment.dto.request;

import com.chatterjee.sayan.payzapp.common.entities.Money;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;

import java.time.LocalDateTime;
import java.util.Map;

public record CreateOrderRequest(
        @Column(nullable = false)
        Money amount,
        @Column(length = 200)
        String receipt, // order_id that is known to merchant
        Map<String,Object> notes,
        LocalDateTime expiresAt
) {
}
