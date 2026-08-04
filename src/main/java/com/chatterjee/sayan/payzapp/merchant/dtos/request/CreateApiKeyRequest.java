package com.chatterjee.sayan.payzapp.merchant.dtos.request;

import com.chatterjee.sayan.payzapp.common.enums.Environment;

public record CreateApiKeyRequest(

        Environment environment
) {
}
