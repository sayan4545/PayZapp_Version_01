package com.chatterjee.sayan.payzapp.vault.dto.response;

import com.chatterjee.sayan.payzapp.common.enums.CardBrand;

public record TokenizeResponse(

        String token,
        String lastFour,
        CardBrand cardBrand,
        Integer expiryMonth,
        Integer expiryYear
) {
}
