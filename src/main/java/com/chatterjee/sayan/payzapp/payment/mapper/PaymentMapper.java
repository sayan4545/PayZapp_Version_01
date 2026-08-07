package com.chatterjee.sayan.payzapp.payment.mapper;

import com.chatterjee.sayan.payzapp.payment.dto.response.PaymentResponse;
import com.chatterjee.sayan.payzapp.payment.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    // From Payment to PaymentResponse
    @Mapping(target = "orderId", source = "order.id")
    PaymentResponse toResponse(Payment payment);

    // From list of payments to list od paymentResponse
    @Mapping(target = "orderId",source = "order.id")
    List<PaymentResponse> toResponseList(List<Payment> payments);
}
