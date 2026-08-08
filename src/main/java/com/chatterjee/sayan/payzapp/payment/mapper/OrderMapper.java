package com.chatterjee.sayan.payzapp.payment.mapper;

import com.chatterjee.sayan.payzapp.payment.dto.response.OrderResponse;
import com.chatterjee.sayan.payzapp.payment.entities.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderResponse toOrderResponse(OrderRecord order);
}
