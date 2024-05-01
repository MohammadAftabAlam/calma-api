package com.calmaapp.authentication;

import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SecretKeyGenerator {

    private static SecretKey secretKey;

    public static SecretKey getSecretKey() {
        if (secretKey == null) {
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA512");
                keyGenerator.init(512); // Set the desired key length
                secretKey = keyGenerator.generateKey();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Failed to generate secret key", e);
            }
        }
        return secretKey;
    }
}
