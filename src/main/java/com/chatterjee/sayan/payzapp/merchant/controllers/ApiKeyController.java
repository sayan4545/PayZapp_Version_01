package com.chatterjee.sayan.payzapp.merchant.controllers;

import com.chatterjee.sayan.payzapp.merchant.dtos.request.CreateApiKeyRequest;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.ApiKeyGetResponse;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.ApiKeyResponse;
import com.chatterjee.sayan.payzapp.merchant.entities.ApiKey;
import com.chatterjee.sayan.payzapp.merchant.services.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/merchants/{merchantId}/api-keys")
@RequiredArgsConstructor
@Slf4j
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping("/create")
    public ResponseEntity<ApiKeyResponse> createApiKey(@PathVariable UUID merchantId, @Valid @RequestBody CreateApiKeyRequest createApiKeyRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiKeyService.createApiKey(merchantId,createApiKeyRequest));
    }


    @GetMapping
    public ResponseEntity<List<ApiKeyGetResponse>> listByMerchant(@PathVariable UUID merchantId){
        return ResponseEntity.status(HttpStatus.FOUND).body(apiKeyService.getAllApiKeys(merchantId));
    }

    @DeleteMapping("/{keyId}")
    public ResponseEntity<Void> revoke(@PathVariable UUID merchantId, @PathVariable UUID keyId){

        apiKeyService.revoke(merchantId,keyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{keyId}/rotate")
    public ResponseEntity<ApiKeyResponse> rotate(@PathVariable UUID keyId,@PathVariable UUID merchantId){
        return ResponseEntity.status(HttpStatus.OK).body(apiKeyService.rotateApiKey(merchantId,keyId));
    }


}
