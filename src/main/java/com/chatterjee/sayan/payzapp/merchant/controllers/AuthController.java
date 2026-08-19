package com.chatterjee.sayan.payzapp.merchant.controllers;

import com.chatterjee.sayan.payzapp.merchant.dtos.request.LoginRequestDto;
import com.chatterjee.sayan.payzapp.merchant.dtos.request.MerchantSignUpRequest;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.LoginResponse;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.MerchantResponse;
import com.chatterjee.sayan.payzapp.merchant.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<MerchantResponse> signUp(@Valid @RequestBody  MerchantSignUpRequest merchantSignUpRequest){
        return new ResponseEntity<>(authService.signUp(merchantSignUpRequest), HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }


}
