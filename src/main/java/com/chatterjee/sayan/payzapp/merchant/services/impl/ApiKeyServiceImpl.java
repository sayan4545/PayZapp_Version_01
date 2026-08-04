package com.chatterjee.sayan.payzapp.merchant.services.impl;

import com.chatterjee.sayan.payzapp.common.exceptions.ResourceNotFoundException;
import com.chatterjee.sayan.payzapp.merchant.dtos.request.CreateApiKeyRequest;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.ApiKeyResponse;
import com.chatterjee.sayan.payzapp.merchant.entities.ApiKey;
import com.chatterjee.sayan.payzapp.merchant.entities.Merchant;
import com.chatterjee.sayan.payzapp.merchant.repositories.ApiKeyRepository;
import com.chatterjee.sayan.payzapp.merchant.repositories.MerchantRepository;
import com.chatterjee.sayan.payzapp.merchant.services.ApiKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final MerchantRepository merchantRepository;

    @Override
    public ApiKeyResponse createApiKey(UUID merchantId, CreateApiKeyRequest createApiKeyRequest) {

        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(()-> new ResourceNotFoundException("merchant",merchantId));

        String keyId = "pzp_"+createApiKeyRequest.environment().name().toUpperCase()+"big_random_string";// TODO : generate a Random string
        String rawSecret = "big_random_secret"; // TODO : use cryptographic random hex

        ApiKey apiKey = ApiKey
                .builder()
                .keyId(keyId)
                .merchant(merchant)
                .keySecretHash(rawSecret)
                .environment(createApiKeyRequest.environment())
                .build();

        apiKeyRepository.save(apiKey);

        return new ApiKeyResponse(apiKey.getId(),apiKey.getKeyId(),rawSecret,apiKey.getEnvironment());

    }
}
