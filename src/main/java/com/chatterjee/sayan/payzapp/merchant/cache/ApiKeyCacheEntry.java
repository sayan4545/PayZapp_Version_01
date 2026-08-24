package com.chatterjee.sayan.payzapp.merchant.cache;

import com.chatterjee.sayan.payzapp.common.enums.Environment;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApiKeyCacheEntry(
        String keyId,
        String keySecretHash,
        String previousKeySecretHash,
        LocalDateTime gracePeriodExpiresAt,
        UUID merchantId,
        Environment environment,
        boolean enabled
) {

    // to check if in grace period
    public boolean isInGracePeriod(){
        return gracePeriodExpiresAt != null && LocalDateTime.now().isBefore(gracePeriodExpiresAt);
    }
}
