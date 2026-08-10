package com.chatterjee.sayan.payzapp.payment.config;

import com.chatterjee.sayan.payzapp.common.enums.PaymentMethod;
import com.chatterjee.sayan.payzapp.payment.gateway.Adapter.PaymentAdapter;
import com.chatterjee.sayan.payzapp.payment.gateway.Adapter.impl.CardPaymentAdapter;
import com.chatterjee.sayan.payzapp.payment.gateway.Adapter.impl.InternetBankingPaymentAdapter;
import com.chatterjee.sayan.payzapp.payment.gateway.Adapter.impl.UpiPaymentAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentAdapterConfig {

    private final InternetBankingPaymentAdapter internetBankingPaymentAdapter;
    private final UpiPaymentAdapter upiPaymentAdapter;
    private final CardPaymentAdapter cardPaymentAdapter;

    @Bean
    public Map<PaymentMethod, PaymentAdapter> paymentMap(){
        return Map.of(
                PaymentMethod.CARD, cardPaymentAdapter,
                PaymentMethod.INTERNET_BANKING,internetBankingPaymentAdapter,
                PaymentMethod.UPI,upiPaymentAdapter
        );
    }
}
