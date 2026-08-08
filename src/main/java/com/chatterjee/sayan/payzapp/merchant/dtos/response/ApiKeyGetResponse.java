package com.chatterjee.sayan.payzapp.merchant.dtos.response;

import com.chatterjee.sayan.payzapp.common.enums.Environment;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiKeyGetResponse(


        UUID id,
        String keyId,
        Environment environment,
        boolean enabled,
        LocalDateTime lastUsedAt,
        LocalDateTime createdAt
) {
}

