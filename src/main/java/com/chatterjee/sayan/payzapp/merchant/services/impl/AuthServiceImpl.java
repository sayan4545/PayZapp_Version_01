package com.chatterjee.sayan.payzapp.merchant.services.impl;

import com.chatterjee.sayan.payzapp.common.enums.MerchantStatus;
import com.chatterjee.sayan.payzapp.common.enums.UserRole;
import com.chatterjee.sayan.payzapp.common.exceptions.DuplicateResourceException;
import com.chatterjee.sayan.payzapp.merchant.dtos.request.MerchantSignUpRequest;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.MerchantResponse;
import com.chatterjee.sayan.payzapp.merchant.entities.AppUser;
import com.chatterjee.sayan.payzapp.merchant.entities.Merchant;
import com.chatterjee.sayan.payzapp.merchant.repositories.AppUserRepository;
import com.chatterjee.sayan.payzapp.merchant.repositories.MerchantRepository;
import com.chatterjee.sayan.payzapp.merchant.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MerchantRepository merchantRepository;
    private final AppUserRepository appUserRepository;


    @Override
    @Transactional
    public MerchantResponse signUp(MerchantSignUpRequest merchantSignUpRequest) {
        // Check if the merchant already exists or not
        if(merchantRepository.existsByEmail(merchantSignUpRequest.email())){
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL","Email already exists :" +  merchantSignUpRequest.email());
        }

        // create a merchant
        Merchant merchant = Merchant.builder()
                .name(merchantSignUpRequest.name())
                .email(merchantSignUpRequest.email())
                .BusinessName(merchantSignUpRequest.businessName())
                .merchantStatus(MerchantStatus.PENDING_KYC)
                .businessType(merchantSignUpRequest.businessType())
                .build();
        // Persist the merchant

        merchantRepository.save(merchant);
        // create an appuser simultaneously

        AppUser appUser = AppUser.builder()
                .email(merchantSignUpRequest.email())
                .passwordHash(merchantSignUpRequest.password()) // TODO : encrypt the password
                .merchant(merchant)
                .role(UserRole.OWNER)
                .build();

        // Persist the appuser to the repo

        appUserRepository.save(appUser);

        // TODO : Use mapstruct to design the response type

        return new MerchantResponse(merchant.getId(), merchant.getName(),
                merchant.getName(), merchant.getBusinessName(),merchant.getBusinessType()
        ,merchant.getMerchantStatus());
    }
}
