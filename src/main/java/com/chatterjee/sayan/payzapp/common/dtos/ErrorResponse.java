package com.chatterjee.sayan.payzapp.common.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(

        String errorCode,
        String errorDescription,
        LocalDateTime timeStamp,
        List<FieldErrors> fieldErrors
) {
    public static ErrorResponse of(String errorCode, String errorDescription){
        return new ErrorResponse(errorCode,errorDescription,LocalDateTime.now(),null);
    }

    public static ErrorResponse of(String errorCode, String errorDescription,List<FieldErrors> fieldErrors){
        return new ErrorResponse(errorCode,errorDescription,LocalDateTime.now(),fieldErrors);
    }
}
