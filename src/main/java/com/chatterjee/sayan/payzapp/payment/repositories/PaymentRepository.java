package com.chatterjee.sayan.payzapp.payment.repositories;

import com.chatterjee.sayan.payzapp.common.enums.PaymentStatus;
import com.chatterjee.sayan.payzapp.payment.entities.OrderRecord;
import com.chatterjee.sayan.payzapp.payment.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByOrder(OrderRecord order);

    List<Payment> findByOrder_Id(OrderRecord order);

    Optional<Payment> findByIdAndMerchantId(UUID merchantId, UUID paymentId);

    List<Payment> findByPaymentStatusAndCreatedAtBefore(PaymentStatus status, LocalDateTime globalWindow);
}
