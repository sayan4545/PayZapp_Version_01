package com.chatterjee.sayan.payzapp.vault.dto.request;

import com.chatterjee.sayan.payzapp.vault.validation.ExpiryYear;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.LuhnCheck;
import org.mapstruct.Mapper;

import java.util.UUID;

public record TokenizeRequest(
        @NotBlank(message = "Pan is mandatory")
        @LuhnCheck(message = "Invalid card number")
        @Pattern(regexp = "^[0-9]{13,19}$",message = "PAN length is between 13 and 19")
        String pan,

        @NotBlank(message = "CVV is mandatory")
        @Pattern(regexp = "^[0-9]{3,4}$", message = "length of cvv is either 3 OR 4")
        String cvv,

        @NotNull(message = "Expiry month is mandatory")
        @Min(value = 1 , message = "Month starts from 1")
        @Max(value = 12, message = "Month ends at 12")
        Integer expiryMonth,

        @NotNull(message = "Expiry Year is mandatory")
        @ExpiryYear
        Integer expiryYear,

        UUID customerId,
        @Min(1)
        @Max(30)
        String cardHolderName
) {
}
