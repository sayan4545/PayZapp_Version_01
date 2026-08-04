package com.chatterjee.sayan.payzapp.common.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomizerUtil {

    public static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String randomBase64(int length){
        byte[] buf = new byte[length/2];
        SECURE_RANDOM.nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }
}
