package com.chatterjee.sayan.payzapp.payment.statemachine.services.impl;

import com.chatterjee.sayan.payzapp.common.enums.PaymentActor;
import com.chatterjee.sayan.payzapp.common.enums.PaymentEvent;
import com.chatterjee.sayan.payzapp.common.enums.PaymentStatus;
import com.chatterjee.sayan.payzapp.payment.entities.Payment;
import com.chatterjee.sayan.payzapp.payment.entities.PaymentTransitionLog;
import com.chatterjee.sayan.payzapp.payment.repositories.PaymentTransitionLogRepository;
import com.chatterjee.sayan.payzapp.payment.statemachine.PaymentStateMachine;
import com.chatterjee.sayan.payzapp.payment.statemachine.services.PaymentTransitionLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentTransitionLogServiceImpl implements PaymentTransitionLogService {

    private final PaymentTransitionLogRepository paymentTransitionLogRepository;
    private final PaymentStateMachine paymentStateMachine;
    @Override
    public PaymentStatus apply(Payment payment, PaymentEvent event) {
        PaymentStatus next = paymentStateMachine
                .transition(payment.getPaymentStatus(), event);
        payment.setPaymentStatus(next);

        // Build a payment transition log
        PaymentTransitionLog paymentTransitionLog = PaymentTransitionLog.builder()
                .payment(payment)
                .fromStatus(payment.getPaymentStatus())
                .paymentEvent(event)
                .toStatus(next)
                .actor(PaymentActor.SYSTEM) // TODO : fetch from merchant context
                .occuredAt(LocalDateTime.now())
                .build();
        paymentTransitionLogRepository.save(paymentTransitionLog);
        return next;
    }
}
