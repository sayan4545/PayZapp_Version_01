package com.chatterjee.sayan.payzapp.vault.validation.impl;

import com.chatterjee.sayan.payzapp.vault.validation.ExpiryYear;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.UUID;

@Service
public class ExpiryYearValidator implements ConstraintValidator<ExpiryYear, Integer> {
    @Override
    public boolean isValid(Integer inputYear, ConstraintValidatorContext context) {
        if(inputYear == null) {
            return false;
        }
        int currentYear = Year.now().getValue();
        return inputYear >= currentYear;
    }
}
