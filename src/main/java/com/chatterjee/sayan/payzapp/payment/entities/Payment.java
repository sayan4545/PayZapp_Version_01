package com.chatterjee.sayan.payzapp.payment.entities;

import com.chatterjee.sayan.payzapp.common.entities.Money;
import com.chatterjee.sayan.payzapp.common.enums.PaymentMethod;
import com.chatterjee.sayan.payzapp.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static jakarta.persistence.GenerationType.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "payments",indexes = {
        @Index(name = "idx_payment_order_id",columnList = "order_id"),
        @Index(name = "idx_payment_merchant_id",columnList = "merchant_id")
})
public class Payment {

    @Id
    @GeneratedValue(strategy = UUID)
    private UUID id;
    @Embedded
    private Money money;

    // One order can have many payments . Many payments to One order

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "order_id",nullable = false)
    private OrderRecord order;

    @Column(nullable = false)
    private UUID merchantId;

    @Column(nullable = false,length = 100)
    private String idempotencyKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private PaymentMethod paymentMethod;


    @JdbcTypeCode((SqlTypes.JSON))
    @Column(columnDefinition = "jsonb",name = "method_details")
    private Map<String,Object> methodDetails;


    @Column(length = 100)
    private String bankReference;

    @Column(length = 100)
    private String errorCode;

    @Column(length = 100)
    private String errorDescription;

    private LocalDateTime authorizedAt;

    private LocalDateTime capturedAt;

    private LocalDateTime failedAt;

    private LocalDateTime refundedAt;

    private LocalDateTime settledAt;

}
