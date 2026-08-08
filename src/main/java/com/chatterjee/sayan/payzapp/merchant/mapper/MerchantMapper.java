package com.chatterjee.sayan.payzapp.merchant.mapper;

import com.chatterjee.sayan.payzapp.merchant.dtos.request.MerchantSignUpRequest;
import com.chatterjee.sayan.payzapp.merchant.dtos.response.MerchantResponse;
import com.chatterjee.sayan.payzapp.merchant.entities.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {

    Merchant toEntity(MerchantSignUpRequest request);
    MerchantResponse toResponse(Merchant merchant);
}
