package com.chatterjee.sayan.payzapp.operations.entities;

import com.chatterjee.sayan.payzapp.common.entities.BaseEntity;
import com.chatterjee.sayan.payzapp.payment.entities.Payment;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "settlement_payment")
@Builder
public class SettlementPayment extends BaseEntity {

    @EmbeddedId
    private SettlementPaymentId settlementPaymentId;

//    @MapsId()
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "settlement_id",nullable = false)
//    private Settlement settlement;

//    @MapsId("settlementId")
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "settlement_id", nullable = false)
//    private Settlement settlement;



}
