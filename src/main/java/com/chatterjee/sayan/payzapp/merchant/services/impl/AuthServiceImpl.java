package com.chatterjee.sayan.payzapp.merchant.services.impl;

import com.chatterjee.sayan.payzapp.common.enums.MerchantStatus;
import com.chatterjee.sayan.payzapp.common.enums.UserRole;
import com.chatterjee.sayan.payzapp.common.exceptions.DuplicateResourceException;
import com.chatterjee.sayan.payzapp.common.exceptions.ResourceNotFoundException;
import com.chatterjee.sayan.payzapp.merchant.dtos.request.LoginRequestDto;
import com.chatterjee.sayan.payzapp.merchant.dtos.request.MerchantSignUpRequest;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.LoginResponse;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.MerchantResponse;
import com.chatterjee.sayan.payzapp.merchant.entities.AppUser;
import com.chatterjee.sayan.payzapp.merchant.entities.Merchant;
import com.chatterjee.sayan.payzapp.merchant.mapper.MerchantMapper;
import com.chatterjee.sayan.payzapp.merchant.repositories.AppUserRepository;
import com.chatterjee.sayan.payzapp.merchant.repositories.MerchantRepository;
import com.chatterjee.sayan.payzapp.merchant.security.JwtUtils;
import com.chatterjee.sayan.payzapp.merchant.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final MerchantRepository merchantRepository;
    private final AppUserRepository appUserRepository;
    private final MerchantMapper merchantMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;



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
                .businessName(merchantSignUpRequest.businessName())
                .merchantStatus(MerchantStatus.PENDING_KYC)
                .businessType(merchantSignUpRequest.businessType())
                .build();
       // Merchant merchant = merchantMapper.toEntity(merchantSignUpRequest);
        merchant.setMerchantStatus(MerchantStatus.PENDING_KYC);
        // Persist the merchant

        merchantRepository.save(merchant);
        // create an appuser simultaneously

        AppUser appUser = AppUser.builder()
                .email(merchantSignUpRequest.email())
                .passwordHash(passwordEncoder.encode(merchantSignUpRequest.password()))
                .merchant(merchant)
                .role(UserRole.OWNER)
                .build();

        // Persist the appuser to the repo

        appUserRepository.save(appUser);



        return new MerchantResponse(merchant.getId(), merchant.getName(),
                merchant.getEmail(), merchant.getBusinessName(),merchant.getBusinessType()
                ,merchant.getMerchantStatus());
      //  return merchantMapper.toResponse(merchant);
    }

    @Override
    public LoginResponse login(LoginRequestDto loginRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password())
        );
        AppUser appUser = appUserRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(()-> new ResourceNotFoundException("user", loginRequestDto.email()));

        String token = jwtUtils.generateAccessToken(loginRequestDto.email(),
                appUser.getMerchant().getId(),appUser.getRole().toString());
        return new LoginResponse(token);
    }
}
