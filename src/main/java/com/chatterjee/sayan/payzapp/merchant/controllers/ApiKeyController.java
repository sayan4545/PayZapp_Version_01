package com.chatterjee.sayan.payzapp.merchant.controllers;

import com.chatterjee.sayan.payzapp.merchant.dtos.request.CreateApiKeyRequest;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.ApiKeyGetResponse;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.ApiKeyResponse;
import com.chatterjee.sayan.payzapp.merchant.entities.ApiKey;
import com.chatterjee.sayan.payzapp.merchant.security.MerchantContext;
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
@RequestMapping("/v1/merchants/api-keys")
@RequiredArgsConstructor
@Slf4j
public class ApiKeyController {

    private final ApiKeyService apiKeyService;
    private final MerchantContext  merchantContext;

    @PostMapping("/create")
    public ResponseEntity<ApiKeyResponse> createApiKey( @Valid @RequestBody CreateApiKeyRequest createApiKeyRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiKeyService.createApiKey(merchantContext.getMerchantId(),createApiKeyRequest));
    }


    @GetMapping
    public ResponseEntity<List<ApiKeyGetResponse>> listByMerchant(){
        return ResponseEntity.status(HttpStatus.FOUND).body(apiKeyService.getAllApiKeys(merchantContext.getMerchantId()));
    }

    @DeleteMapping("/{keyId}")
    public ResponseEntity<Void> revoke( @PathVariable UUID keyId){

        apiKeyService.revoke(merchantContext.getMerchantId(),keyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{keyId}/rotate")
    public ResponseEntity<ApiKeyResponse> rotate(@PathVariable UUID keyId){
        return ResponseEntity.status(HttpStatus.OK).body(apiKeyService.rotateApiKey(merchantContext.getMerchantId(),keyId));
    }


}
