package com.chatterjee.sayan.payzapp.payment.config;

import com.chatterjee.sayan.payzapp.common.enums.PaymentMethod;
import com.chatterjee.sayan.payzapp.payment.processor.PaymentProcessor;
import com.chatterjee.sayan.payzapp.payment.processor.strategy.CardPaymentProcessor;
import com.chatterjee.sayan.payzapp.payment.processor.strategy.InternetBankingPaymentProcessor;
import com.chatterjee.sayan.payzapp.payment.processor.strategy.UPIPaymentsProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PaymentProcessorConfig {

    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessorMap() {
        return Map.of(PaymentMethod.INTERNET_BANKING, new InternetBankingPaymentProcessor(),
                    PaymentMethod.CARD, new CardPaymentProcessor(),
                PaymentMethod.UPI,new UPIPaymentsProcessor());
    };
}
