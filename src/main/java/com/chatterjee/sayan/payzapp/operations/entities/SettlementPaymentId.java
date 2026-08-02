package com.chatterjee.sayan.payzapp.operations.entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class SettlementPaymentId implements Serializable {

    private UUID settlementId;
    private UUID paymentId;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SettlementPaymentId that)) return false;
        return Objects.equals(settlementId, that.settlementId) && Objects.equals(paymentId, that.paymentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(settlementId, paymentId);
    }
}
