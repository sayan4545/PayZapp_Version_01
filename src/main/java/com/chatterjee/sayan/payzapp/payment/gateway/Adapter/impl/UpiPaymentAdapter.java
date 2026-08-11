package com.chatterjee.sayan.payzapp.payment.gateway.Adapter.impl;

import com.chatterjee.sayan.payzapp.common.enums.PaymentMethod;
import com.chatterjee.sayan.payzapp.payment.gateway.Adapter.PaymentAdapter;
import com.chatterjee.sayan.payzapp.payment.gateway.dto.PaymentRequest;
import com.chatterjee.sayan.payzapp.payment.gateway.dto.PaymentResult;
import com.chatterjee.sayan.payzapp.payment.processor.PaymentProcessorRouter;
import com.chatterjee.sayan.payzapp.payment.processor.dto.PaymentProcessorRequest;
import com.chatterjee.sayan.payzapp.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpiPaymentAdapter implements PaymentAdapter {
    private final PaymentProcessorRouter paymentProcessorRouter;
    @Override
    public PaymentResult initiate(PaymentRequest paymentRequest) {
        log.info("Initiating payment with UPI adapter, paymentId : {}",paymentRequest.paymentId());
        try {
            PaymentProcessorRequest paymentProcessorRequest = PaymentProcessorRequest.nonCard(
                    paymentRequest.paymentId(),
                    PaymentMethod.UPI,
                    paymentRequest.amount(),
                    paymentRequest.methodDetails()
            );

            PaymentProcessorResponse paymentProcessorResponse = paymentProcessorRouter.charge(paymentProcessorRequest);

            return switch (paymentProcessorResponse) {
                case PaymentProcessorResponse.Failed failed ->
                        new PaymentResult.Failure(failed.errorCode(), failed.errorDescription());
                case PaymentProcessorResponse.Pending pending ->
                        new PaymentResult.Pending(pending.processorReference());
                case PaymentProcessorResponse.Success success -> new PaymentResult.Success(success.bankReference());
            };

        }catch (Exception e){
            log.warn("UPI failed, paymentId : {}",paymentRequest.paymentId());
            return new PaymentResult.Failure("UPI failed",e.getMessage());
        }

    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return new PaymentResult.Success("VPA_SUCCESS_REF");
    }
}
