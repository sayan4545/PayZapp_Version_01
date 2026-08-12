package com.chatterjee.sayan.payzapp.payment.processor.strategy;

import com.chatterjee.sayan.payzapp.common.utils.RandomizerUtil;
import com.chatterjee.sayan.payzapp.payment.processor.PaymentProcessor;
import com.chatterjee.sayan.payzapp.payment.processor.dto.PaymentProcessorRequest;
import com.chatterjee.sayan.payzapp.payment.processor.dto.PaymentProcessorResponse;
import org.springframework.stereotype.Component;

@Component
public class UPIPaymentsProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        final String VPA_PAYMENT_FAILED = "VPA_PAYMENT_FAILED";
        String bankCode = request.methodDetails()!=null?
                request.methodDetails().get("VPA").toString():null;

        if(VPA_PAYMENT_FAILED.equals(bankCode)){
            return new PaymentProcessorResponse.Failed("UPI REJECETED",
                    "VPA_PAYMENT_FAILED");
        }

        String processorRef = "UPI_PROCESSOR"+ RandomizerUtil.randomBase64(16);
        //String BankRef = "BANK_REF"+RandomizerUtil.randomBase64(16);

        return new PaymentProcessorResponse.Pending(processorRef);
    }
}
