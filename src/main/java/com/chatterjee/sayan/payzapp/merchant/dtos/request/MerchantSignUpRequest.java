package com.chatterjee.sayan.payzapp.merchant.dtos.request;

import com.chatterjee.sayan.payzapp.common.enums.BusinessType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MerchantSignUpRequest(
        @NotNull(message = "Name should be provided")
        @Size(max = 100, message = "Name should be of maximum 100 characters")
        String name,

        @Email
        @NotNull(message = "Email is required")
        String email,

        @NotNull(message = "Password is required")
        @Size(min = 8, max = 100, message = "Password should be minimum 8 charactres and maximum 100 characters")
        String password,

        @Size(max = 50, message = "BusinessName should be maximum 50 characters long")
        String businessName,

        BusinessType businessType
) {
}
