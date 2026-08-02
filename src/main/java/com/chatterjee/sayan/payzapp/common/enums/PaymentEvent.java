package com.chatterjee.sayan.payzapp.common.enums;

public enum PaymentEvent {
    AUTHORIZE_ATTEMPT,
    AUTHORIZE_SUCCESS,
    AUTHORIZE_FAILURE,
    CAPTURE_REQUEST,
    CAPTURE_SUCCESS,
    CAPTURE_FAILURE,
    REFUND_INIT,
    REFUND_COMPLETE,
    SETTLE,
    CAPTURE,
    CAPTURE_TIMEOUT
}
