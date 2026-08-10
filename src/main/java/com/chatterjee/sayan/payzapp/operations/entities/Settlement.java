package com.chatterjee.sayan.payzapp.operations.entities;

import com.chatterjee.sayan.payzapp.common.entities.BaseEntity;
import com.chatterjee.sayan.payzapp.common.entities.Money;
import com.chatterjee.sayan.payzapp.common.enums.SettlementStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "settlements")
public class Settlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountInUnits",column = @Column(name = "gross_amount_in_units",nullable = false)),
            @AttributeOverride(name = "currency",column = @Column(name = "gross_amount_currency",nullable = false))
    })
    @Column(nullable = false)
    private Money grossAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountInUnits",column = @Column(name = "refund_amount_in_units",nullable = false)),
            @AttributeOverride(name = "currency",column = @Column(name = "refund_amount_currency",nullable = false))
    })
    @Column(nullable = false)
    private Money refundAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountInUnits",column = @Column(name = "gst_amount_in_units",nullable = false)),
            @AttributeOverride(name = "currency",column = @Column(name = "gst_amount_currency",nullable = false))
    })
    @Column(nullable = false)
    private Money gstAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountInUnits",column = @Column(name = "fee_amount_in_units",nullable = false)),
            @AttributeOverride(name = "currency",column = @Column(name = "fee_amount_currency",nullable = false))
    })
    @Column(nullable = false)
    private Money feeAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountInUnits",column = @Column(name = "net_amount_in_units",nullable = false)),
            @AttributeOverride(name = "currency",column = @Column(name = "net_amount_currency",nullable = false))
    })
    @Column(nullable = false)
    private Money netAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 10)
    private SettlementStatus settlementStatus;


    @Column(nullable = false)
    private String bankReference;

    private LocalDateTime processedAt;




}
