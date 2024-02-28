package com.calmaapp.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.calmaapp.entity.User;

import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
// import com.calmaapp.authentication.*;

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

    // @Value("${jwt.secret}") // Inject the secret key from application.properties
    // private String secretKey;

    // public Long getUserIdFromToken(String token) {
    //     try {
    //         Claims claims = Jwts.parser()
    //                             .setSigningKey(secretKey)
    //                             .parseClaimsJws(token)
    //                             .getBody();
    
    //         // Get the subject from claims
    //         String subject = claims.getSubject();
    
    //         // Parse the subject directly as Long
    //         Long userId = Long.valueOf(subject);

    
    //         return userId;
    //     } catch (JwtException | NumberFormatException e) {
    //         // Handle JWT parsing exception or NumberFormatException
    //         throw new IllegalArgumentException("Invalid JWT token or user ID format", e);
    //     }
    // }
    
    // public String getUsernameFromToken(String token) {
    //     try {
    //         Claims claims = Jwts.parser()
    //                             .setSigningKey(secretKey)
    //                             .parseClaimsJws(token)
    //                             .getBody();
    
    //         // Extract the username from the token claims
    //         String username = claims.getSubject();
    //         return username;
    //     } catch (JwtException e) {
    //         // Handle JWT parsing exception
    //         throw new IllegalArgumentException("Invalid JWT token", e);
    //     }
    // }
    

    public String getPhoneNumberFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        String encodedPhoneNumber = claims.getSubject();
        // Decode the Base64 encoded phone number
        String phoneNumber = new String(Base64.getDecoder().decode(encodedPhoneNumber));
        return phoneNumber;
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
    
  

   
    public String doGenerateToken(Map<String, Object> claims, String username) {
        // Encode the username using Base64 encoding
        String encodedSubject = Base64.getEncoder().encodeToString(username.getBytes(StandardCharsets.UTF_8));
    
        // Encode any non-base64-safe claim values
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            if (!isBase64Safe(entry.getValue())) {
                entry.setValue(Base64.getEncoder().encodeToString(entry.getValue().toString().getBytes(StandardCharsets.UTF_8)));
            }
        }
    
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(encodedSubject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
    


    private boolean isBase64Safe(Object value) {
        return ((String) value).matches("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{4})$");
    }

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    // public Boolean validateToken(String token, UserDetails userDetails) {
    //     final String username = getUsernameFromToken(token);
    //     return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    // }
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String phoneNumber = getPhoneNumberFromToken(token);
        return (phoneNumber.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    

    public String generateToken(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
    
        Map<String, Object> claims = new HashMap<>();
        // Use username instead of user ID
        String phoneNumber = user.getPhoneNumber();
        if (!isBase64Safe(phoneNumber)) {
            phoneNumber = Base64.getEncoder().encodeToString(phoneNumber.getBytes(StandardCharsets.UTF_8));
        }
        claims.put("phoneNumber", phoneNumber); // Assuming getUsername() returns the username
    
        // Add additional claims or customization as needed
    
        return doGenerateToken(claims, user.getPhoneNumber()); // Pass the username as the subject
    }
    

}
