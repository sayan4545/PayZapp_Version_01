package com.chatterjee.sayan.payzapp.merchant.dtos.response;

import com.chatterjee.sayan.payzapp.common.enums.Environment;

import java.util.UUID;

public record ApiKeyResponse(

        UUID id,
        String keyId,
        String keySecret,
        Environment environment
) {
}
