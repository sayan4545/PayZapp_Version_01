package com.chatterjee.sayan.payzapp.payment.entities;

import com.chatterjee.sayan.payzapp.common.entities.Money;
import com.chatterjee.sayan.payzapp.common.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "order_records",indexes = {
        @Index(name = "idx_order_record_merchant_id_id",columnList = "id,merchant_id"),
        @Index(name = "idx_order_record_merchant_id",columnList = "merchant_id")
})
public class OrderRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    // no FK - cross service boundary
    @Column(nullable = false,name = "merchant_id")
    private UUID merchantId;

    @Embedded
    private Money amount;

    @Column(length = 200)
    private String receipt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private OrderStatus orderStatus = OrderStatus.CREATED;

    @Column(nullable = false)
    @Builder.Default
    private Integer attempts = 0;

    @JdbcTypeCode((SqlTypes.JSON))
    @Column(columnDefinition = "jsonb")
    private Map<String, Object>  notes;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime expiresAt;
}
