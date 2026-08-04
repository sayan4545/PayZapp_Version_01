package com.chatterjee.sayan.payzapp.merchant.dtos.response;

import com.chatterjee.sayan.payzapp.common.enums.Environment;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApiKeyGetResponse(


        UUID id,
        String keyId,
        Environment environment,
        boolean enabled,
        LocalDateTime lastUsedAt,
        LocalDateTime createdAt
) {
}

