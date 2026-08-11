package com.chatterjee.sayan.payzapp.payment.repositories;

import com.chatterjee.sayan.payzapp.payment.entities.PaymentTransitionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentTransitionLogRepository extends JpaRepository<PaymentTransitionLog, UUID> {
}
