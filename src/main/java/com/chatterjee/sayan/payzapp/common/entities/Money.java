package com.chatterjee.sayan.payzapp.common.entities;


import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Money {

    private int amountInUnits;
    private String currency;

//    private Money(int amountInUnits, String currency) {
//        this.amountInUnits = amountInUnits;
//        this.currency = currency;
//    }

    public static Money of(int amountInUnits, String currency) {
        return new Money(amountInUnits, currency);
    }
    public static Money inr(int amountInUnits) {
        return new Money(amountInUnits, "INR");
    }

    public Money add(Money money) {
        if(!this.currency.equals(money.currency)){
            throw new IllegalArgumentException("Currency has wrong value");
        }
        return new Money(this.amountInUnits + money.amountInUnits, this.currency);
    }

    public Money subtract(Money money) {
        if(!this.currency.equals(money.currency)){
            throw new IllegalArgumentException("Currency has wrong value");
        }
        return new Money(this.amountInUnits - money.amountInUnits, this.currency);
    }
}
