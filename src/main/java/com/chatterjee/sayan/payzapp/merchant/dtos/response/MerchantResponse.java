package com.chatterjee.sayan.payzapp.merchant.dtos.response;

import com.chatterjee.sayan.payzapp.common.enums.BusinessType;
import com.chatterjee.sayan.payzapp.common.enums.MerchantStatus;

import java.util.UUID;

public record MerchantResponse(

        UUID id,
        String name,
        String email,
        String businessName,
        BusinessType businessType,
        MerchantStatus merchantStatus

) {
}
