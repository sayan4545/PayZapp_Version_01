package com.chatterjee.sayan.payzapp.vault.service;

import com.chatterjee.sayan.payzapp.vault.dto.request.TokenizeRequest;
import com.chatterjee.sayan.payzapp.vault.dto.response.TokenizeResponse;
import jakarta.validation.Valid;

import java.util.UUID;

public interface VaultService {

    TokenizeResponse tokenize(@Valid TokenizeRequest tokenizeRequest, UUID merchantId);
}
