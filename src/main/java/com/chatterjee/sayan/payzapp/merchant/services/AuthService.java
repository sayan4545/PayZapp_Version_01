package com.chatterjee.sayan.payzapp.merchant.services;

import com.chatterjee.sayan.payzapp.merchant.dtos.request.LoginRequestDto;
import com.chatterjee.sayan.payzapp.merchant.dtos.request.MerchantSignUpRequest;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.LoginResponse;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.MerchantResponse;
import jakarta.validation.Valid;

public interface AuthService {

    MerchantResponse signUp(MerchantSignUpRequest  merchantSignUpRequest);

    LoginResponse login( LoginRequestDto loginRequestDto);
}
