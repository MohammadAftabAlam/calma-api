// package com.calmaapp.authentication;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {
	
// 	 @Bean
// 	    public PasswordEncoder passwordEncoder() {
// 	        return new BCryptPasswordEncoder();
// 	    }

//     @Autowired
//     private JwtAuthenticationEntryPoint point;

//     @Autowired
//     private JwtAuthenticationFilter filter;

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http.csrf(csrf -> csrf.disable())
//                 .authorizeRequests()
//                     .requestMatchers("/api/register").permitAll()
//                     .requestMatchers("/api/login").permitAll()
//                     .requestMatchers("/api/logout").permitAll()
//                     .requestMatchers("/api/salons/*/service", "/api/salons/*/services").hasRole("SALON_OWNER")
//                     .requestMatchers("/api/**").authenticated()
//                     .anyRequest().permitAll().and()
//                     .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
//                     .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//         http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
//         return http.build();
//     }
    
    
// }

package com.calmaapp.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private JwtAuthenticationEntryPoint point;

    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests()
                .requestMatchers("/api/register").permitAll()
                .requestMatchers("/api/login").permitAll()
                .requestMatchers("/api/logout").permitAll()
                .requestMatchers("/api/salons/*/service", "/api/salons/*/services").hasRole("SALON_OWNER")
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
