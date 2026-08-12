package com.chatterjee.sayan.payzapp.payment.processor.strategy;

import com.chatterjee.sayan.payzapp.common.utils.RandomizerUtil;
import com.chatterjee.sayan.payzapp.payment.processor.PaymentProcessor;
import com.chatterjee.sayan.payzapp.payment.processor.dto.PaymentProcessorRequest;
import com.chatterjee.sayan.payzapp.payment.processor.dto.PaymentProcessorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CardPaymentProcessor implements PaymentProcessor {
    public static final  String PAN_CARD_DECLINED = "40000000000002";
    public static final String PAN_CARD_EXPIRED = "40000000000008";
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        String pan = request.pan();

        if(pan.equals(PAN_CARD_DECLINED)) {
            log.warn("Card declined");
            return new PaymentProcessorResponse.Failed("CARD DECLINED","Card declined via bank");
        }
        if(pan.equals(PAN_CARD_EXPIRED)) {
            log.warn("Card expired");
            return new PaymentProcessorResponse.Failed("CARD EXPIRED","Card has expired");
        }
        String processorRef = "CARD_PROCESSOR"+ RandomizerUtil.randomBase64(16);
        return new PaymentProcessorResponse.Pending(processorRef);
    }
}
