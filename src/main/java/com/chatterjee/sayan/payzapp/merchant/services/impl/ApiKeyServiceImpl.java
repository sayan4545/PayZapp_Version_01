package com.chatterjee.sayan.payzapp.merchant.services.impl;

import com.chatterjee.sayan.payzapp.common.exceptions.ResourceNotFoundException;
import com.chatterjee.sayan.payzapp.common.utils.RandomizerUtil;
import com.chatterjee.sayan.payzapp.merchant.dtos.request.CreateApiKeyRequest;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.ApiKeyGetResponse;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.ApiKeyResponse;
import com.chatterjee.sayan.payzapp.merchant.entities.ApiKey;
import com.chatterjee.sayan.payzapp.merchant.entities.Merchant;
import com.chatterjee.sayan.payzapp.merchant.mapper.ApiKeyMapper;
import com.chatterjee.sayan.payzapp.merchant.repositories.ApiKeyRepository;
import com.chatterjee.sayan.payzapp.merchant.repositories.MerchantRepository;
import com.chatterjee.sayan.payzapp.merchant.services.ApiKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final MerchantRepository merchantRepository;
    private final ApiKeyMapper apiKeyMapper;

    @Override
    @Transactional
    public ApiKeyResponse createApiKey(UUID merchantId, CreateApiKeyRequest createApiKeyRequest) {

        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(()-> new ResourceNotFoundException("merchant",merchantId));

        //String keyId = "pzp_"+createApiKeyRequest.environment().name().toUpperCase()+"big_random_string";// TODO : generate a Random string // fixed it
        String keyId = "pzp_"+createApiKeyRequest
                .environment()
                .name()
                .toUpperCase()+
                RandomizerUtil.randomBase64(24);

        //String rawSecret = "big_random_secret"; // TODO : use cryptographic random hex
        String rawSecret = RandomizerUtil.randomBase64(48);

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

    @Override
    public List<ApiKeyGetResponse> getAllApiKeys(UUID merchantId) {

        List<ApiKey> apiKeys = apiKeyRepository.findByMerchant_id(merchantId);

//        return apiKeys.stream()
//                .map(apiKey ->
//                        new ApiKeyGetResponse(apiKey.getId()
//                                ,apiKey.getKeyId(),
//                                apiKey.getEnvironment(),
//                                apiKey.getEnabled(),
//                                apiKey.getLastUsedAt(),
//                                null // TODO : createdAt will be populated after auditing
//                )).toList();

        return apiKeyMapper.toResponseList(apiKeys);

    }

    @Override
    public void revoke(UUID merchantId, UUID keyId) {
        ApiKey apiKey = apiKeyRepository
                .findById(keyId)
                .filter(key-> key.getMerchant().getId().equals(merchantId))
                .orElseThrow(()-> new ResourceNotFoundException("apiKey", keyId));

        apiKey.setEnabled(false);
        apiKeyRepository.save(apiKey);
    }

    @Override
    @Transactional
    public @Nullable ApiKeyResponse rotateApiKey(UUID merchantId, UUID keyId) {
        ApiKey apiKey = apiKeyRepository
                .findById(keyId)
                .filter(key-> key.getMerchant().getId().equals(merchantId))
                .orElseThrow(()-> new ResourceNotFoundException("apiKey", keyId));

        // Check if the api key is revoked or not
        if(!apiKey.getEnabled()) throw new RuntimeException("Cannot rotate a revoked api key");

        String newRawSecret = RandomizerUtil.randomBase64(96);
        apiKey.setPreviousKeySecretHash(apiKey.getKeySecretHash());
        apiKey.setKeySecretHash(newRawSecret);

        apiKey.setRotatedAt(LocalDateTime.now());
        apiKey.setGracePeriodExpiresAt(LocalDateTime.now().plusHours(24));

        apiKeyRepository.save(apiKey);

        return new ApiKeyResponse(apiKey.getId(),apiKey.getKeyId(),newRawSecret,apiKey.getEnvironment());
    }
}
