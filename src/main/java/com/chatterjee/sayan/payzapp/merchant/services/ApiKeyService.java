package com.chatterjee.sayan.payzapp.merchant.services;

import com.chatterjee.sayan.payzapp.merchant.dtos.request.CreateApiKeyRequest;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.ApiKeyGetResponse;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.ApiKeyResponse;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {

    ApiKeyResponse createApiKey(UUID merchantId, CreateApiKeyRequest createApiKeyRequest);

    List<ApiKeyGetResponse> getAllApiKeys(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);


    @Nullable
    ApiKeyResponse rotateApiKey(UUID merchantId, UUID keyId);
}
