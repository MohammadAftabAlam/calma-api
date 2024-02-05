package com.calmaapp.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.calmaapp.entity.User;

import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;


@Component
public class JwtHelper {
	
	
	  public void setUser(User user) {
	        this.user = user;
	    }
	
	private User user;

    public static final long JWT_TOKEN_VALIDITY = 2 * 365 * 24 * 60 * 60; 

    @Autowired
    private SecretKey secretKey; // Inject the SecretKey bean defined in your configuration class

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
  

    public String generateToken(String phoneNumber, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        // Add additional claims or customization as needed
        return doGenerateToken(claims, phoneNumber);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
