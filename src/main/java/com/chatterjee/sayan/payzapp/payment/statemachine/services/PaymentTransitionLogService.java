package com.chatterjee.sayan.payzapp.payment.statemachine.services;

import com.chatterjee.sayan.payzapp.common.enums.PaymentEvent;
import com.chatterjee.sayan.payzapp.common.enums.PaymentStatus;
import com.chatterjee.sayan.payzapp.payment.entities.Payment;

public interface PaymentTransitionLogService {

    PaymentStatus apply(Payment payment, PaymentEvent event);
}
