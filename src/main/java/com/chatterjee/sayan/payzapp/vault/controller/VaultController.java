package com.chatterjee.sayan.payzapp.vault.controller;

import com.chatterjee.sayan.payzapp.vault.dto.request.TokenizeRequest;
import com.chatterjee.sayan.payzapp.vault.dto.response.TokenizeResponse;
import com.chatterjee.sayan.payzapp.vault.service.VaultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/vault")
@RequiredArgsConstructor
public class VaultController {

    private final VaultService vaultService;
    UUID merchantId = UUID.fromString("ghdsljxao0uiuiii-jjdnna");

    @PostMapping("/tokenize")
    public ResponseEntity<TokenizeResponse> tokenize(@Valid @RequestBody TokenizeRequest tokenizeRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(vaultService.tokenize(tokenizeRequest,merchantId));
    }
}
