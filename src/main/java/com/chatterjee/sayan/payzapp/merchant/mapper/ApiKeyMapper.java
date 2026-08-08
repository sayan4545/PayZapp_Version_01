package com.chatterjee.sayan.payzapp.merchant.mapper;

import com.chatterjee.sayan.payzapp.merchant.dtos.response.ApiKeyGetResponse;
import com.chatterjee.sayan.payzapp.merchant.entities.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApiKeyMapper {

    List<ApiKeyGetResponse> toResponseList(List<ApiKey> listOfApiKey);
}
