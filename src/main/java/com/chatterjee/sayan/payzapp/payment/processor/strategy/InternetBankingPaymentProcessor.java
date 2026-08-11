package com.chatterjee.sayan.payzapp.payment.processor.strategy;

import com.chatterjee.sayan.payzapp.common.utils.RandomizerUtil;
import com.chatterjee.sayan.payzapp.payment.processor.PaymentProcessor;
import com.chatterjee.sayan.payzapp.payment.processor.dto.PaymentProcessorRequest;
import com.chatterjee.sayan.payzapp.payment.processor.dto.PaymentProcessorResponse;
import org.springframework.stereotype.Component;

@Component
public class InternetBankingPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        // bank code fail simulation
        // In production, plug in external api

        final String BANK_CODE_FAIL = "BANK_CODE_FAIL";
        String bankCode = request.methodDetails() != null ?
                request.methodDetails().get("BANK").toString() : null;

        if(BANK_CODE_FAIL.equals(bankCode)) {
            return new PaymentProcessorResponse.Failed("BANK REJECTED",
                    "BANK_CODE_FAIL");
        }

        String processorRef = "NBK_PROCESSOR"+ RandomizerUtil.randomBase64(16);
        String redirectRef = "http://REDIRECT_BANK.com/"+processorRef;
        return new PaymentProcessorResponse.Success(processorRef,redirectRef);
    }
}
