package com.chatterjee.sayan.payzapp.vault.service;

import com.chatterjee.sayan.payzapp.common.entities.Money;
import com.chatterjee.sayan.payzapp.payment.processor.dto.PaymentProcessorResponse;
import com.chatterjee.sayan.payzapp.vault.dto.request.TokenizeRequest;
import com.chatterjee.sayan.payzapp.vault.dto.response.TokenizeResponse;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.UUID;

public interface VaultService {

    TokenizeResponse tokenize(TokenizeRequest tokenizeRequest, UUID merchantId);
    PaymentProcessorResponse charge(UUID paymentId,String token, Money amount, Map<String,Object> methodDetails);
}
