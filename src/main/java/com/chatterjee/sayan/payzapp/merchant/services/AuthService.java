package com.chatterjee.sayan.payzapp.merchant.services;

import com.chatterjee.sayan.payzapp.merchant.dtos.request.MerchantSignUpRequest;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.MerchantResponse;

public interface AuthService {

    MerchantResponse signUp(MerchantSignUpRequest  merchantSignUpRequest);
}
