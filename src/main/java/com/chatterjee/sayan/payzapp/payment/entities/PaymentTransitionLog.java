package com.chatterjee.sayan.payzapp.payment.entities;

import com.chatterjee.sayan.payzapp.common.enums.PaymentActor;
import com.chatterjee.sayan.payzapp.common.enums.PaymentEvent;
import com.chatterjee.sayan.payzapp.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_transition_log",indexes = {
        @Index(name = "idx_payment_transition_log",columnList = "payment_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentTransitionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // One payment can have many payment_transition_log
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", nullable = false,length = 20)
    private PaymentStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_event",nullable = false,length = 20)
    private PaymentEvent paymentEvent;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status",nullable = false,length = 20)
    private PaymentStatus toStatus;

    @Column(length = 100,name = "actor")
    @Enumerated(EnumType.STRING)
    private PaymentActor actor;

    @Column(nullable = false,length = 30)
    private LocalDateTime occuredAt;
}
