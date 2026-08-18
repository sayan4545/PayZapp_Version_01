package com.chatterjee.sayan.payzapp.payment.gateway.Adapter.impl;

import com.chatterjee.sayan.payzapp.payment.gateway.Adapter.PaymentAdapter;
import com.chatterjee.sayan.payzapp.payment.gateway.dto.PaymentRequest;
import com.chatterjee.sayan.payzapp.payment.gateway.dto.PaymentResult;
import com.chatterjee.sayan.payzapp.payment.processor.dto.PaymentProcessorResponse;
import com.chatterjee.sayan.payzapp.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CardPaymentAdapter implements PaymentAdapter {

    private final VaultService vaultService;
    @Override
    public PaymentResult initiate(PaymentRequest paymentRequest) {
        String token = (String) paymentRequest.methodDetails().get("token");

        PaymentProcessorResponse response = vaultService.charge(paymentRequest.paymentId(),
                token
                ,paymentRequest.amount()
                ,paymentRequest.methodDetails());

        return switch (response){
            case PaymentProcessorResponse.Success success-> new PaymentResult.Success(success.bankReference());
            case PaymentProcessorResponse.Failed failed-> new PaymentResult.Failure(failed.errorCode(), failed.errorDescription());
            case PaymentProcessorResponse.Pending pending -> new PaymentResult.Pending(pending.processorReference());
        };

    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return new PaymentResult.Success("CARD_REF");
    }
}
