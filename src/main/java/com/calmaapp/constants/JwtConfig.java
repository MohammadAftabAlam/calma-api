package com.calmaapp.constants;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    public SecretKey jwtSecretKey() {
        // Use the configured secret key or generate a new one
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}

