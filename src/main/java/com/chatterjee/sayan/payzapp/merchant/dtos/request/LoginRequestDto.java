package com.chatterjee.sayan.payzapp.merchant.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDto(
        @NotBlank @Email
        String email,

        @NotBlank
        String password
) {
}
