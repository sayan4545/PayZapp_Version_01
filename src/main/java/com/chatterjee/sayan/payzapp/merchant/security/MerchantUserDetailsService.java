package com.chatterjee.sayan.payzapp.merchant.security;

import com.chatterjee.sayan.payzapp.common.exceptions.ResourceNotFoundException;
import com.chatterjee.sayan.payzapp.merchant.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchantUserDetailsService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(username)
                .orElseThrow(()-> new ResourceNotFoundException("User", username));
    }
}
