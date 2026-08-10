package com.chatterjee.sayan.payzapp.payment.gateway;

import com.chatterjee.sayan.payzapp.common.enums.PaymentMethod;
import com.chatterjee.sayan.payzapp.payment.gateway.Adapter.PaymentAdapter;
import com.chatterjee.sayan.payzapp.payment.gateway.dto.PaymentRequest;
import com.chatterjee.sayan.payzapp.payment.gateway.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentGatewayRouter {

    private final Map<PaymentMethod, PaymentAdapter> paymentAdapterMap;

    public PaymentResult initiate(PaymentRequest paymentRequest){
        PaymentAdapter adapter = paymentAdapterMap.get(paymentRequest.paymentMethod());
        if(adapter == null){
            throw new IllegalArgumentException("No payment adapter registered for method"+paymentRequest.paymentMethod());
        }
        return adapter.initiate(paymentRequest);

    }
}
