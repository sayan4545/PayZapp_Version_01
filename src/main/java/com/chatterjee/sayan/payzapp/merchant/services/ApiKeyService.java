package com.chatterjee.sayan.payzapp.merchant.services;

import com.chatterjee.sayan.payzapp.merchant.dtos.request.CreateApiKeyRequest;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.ApiKeyResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

public interface ApiKeyService {

    ApiKeyResponse createApiKey(UUID merchantId, CreateApiKeyRequest createApiKeyRequest);
}
