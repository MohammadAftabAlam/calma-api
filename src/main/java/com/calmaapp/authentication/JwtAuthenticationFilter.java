package com.calmaapp.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.calmaapp.entity.User;
import com.calmaapp.service.CustomUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    

    @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String requestHeader = request.getHeader("Authorization");
    logger.info(" Header :  {}", requestHeader);
    String phoneNumber = null;
    String token = null;
    if (requestHeader != null && requestHeader.startsWith("Bearer")) {
        token = requestHeader.substring(7);
        try {
            phoneNumber = this.jwtHelper.getPhoneNumberFromToken(token);
        } catch (IllegalArgumentException e) {
            logger.info("Illegal Argument while fetching the phone number from token !!");
            e.printStackTrace();
        } catch (ExpiredJwtException e) {
            logger.info("Given jwt token is expired !!");
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            logger.info("Some change has been done in token !! Invalid Token");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } else {
        logger.info("Invalid Header Value !! ");
    }
    if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(phoneNumber);
        Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
        if (validateToken) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            logger.info("Validation fails !!");
        }
    }
    filterChain.doFilter(request, response);
}
}

// @Component
// public class JwtAuthenticationFilter extends OncePerRequestFilter {

//     private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

//     @Autowired
//     private JwtHelper jwtHelper;

//     @Autowired
//     private CustomUserDetailsService customUserDetailsService;

//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//         String requestHeader = request.getHeader("Authorization");
//         logger.info(" Header :  {}", requestHeader);
//         Long userId = null;
//         String token = null;
//         if (requestHeader != null && requestHeader.startsWith("Bearer")) {
//             token = requestHeader.substring(7);
//             try {
//                 userId = this.jwtHelper.getUserIdFromToken(token);
//             } catch (IllegalArgumentException e) {
//                 logger.info("Illegal Argument while fetching the user ID from token !!");
//                 e.printStackTrace();
//             } catch (ExpiredJwtException e) {
//                 logger.info("Given jwt token is expired !!");
//                 e.printStackTrace();
//             } catch (MalformedJwtException e) {
//                 logger.info("Some change has been done in token !! Invalid Token");
//                 e.printStackTrace();
//             } catch (Exception e) {
//                 e.printStackTrace();
//             }
//         } else {
//             logger.info("Invalid Header Value !! ");
//         }
//         if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//             UserDetails userDetails = this.customUserDetailsService.loadUserById(userId);
//             Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
//             if (validateToken) {
//                 UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                 authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                 SecurityContextHolder.getContext().setAuthentication(authentication);
//             } else {
//                 logger.info("Validation fails !!");
//             }
//         }
//         filterChain.doFilter(request, response);
//     }
// }
