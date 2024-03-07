package com.calmaapp.entity;

import com.calmaapp.UserType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private double latitude;
    private double longitude;

    private String resetToken;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Transient
    private String jwtToken;

    private String otp;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    private String name;

    @Column(name = "password")
    private String password;
    private String email;
    private int age;
    private String location;
    private String gender;

    // Implement UserDetails methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add roles based on user type
        if (userType == UserType.CUSTOMER) {
            authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        } else if (userType == UserType.SALON_OWNER) {
            authorities.add(new SimpleGrantedAuthority("ROLE_SALON_OWNER"));
        }

        // Common role for all users
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return authorities;
    }

    @Override
    public String getUsername() {
        return this.phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Implement account expiration logic if needed
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Implement account locking logic if needed
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Implement credentials expiration logic if needed
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Implement account enabling/disabling logic if needed
        return true;
    }

    
   

	public List<String> getRoles() {
		List<String> roles = new ArrayList<>();
        
        // Assuming you have a field named 'userType' in your User entity
        // Map userType to roles
        switch (this.userType) {
            case CUSTOMER:
                roles.add("ROLE_CUSTOMER");
                break;
            case SALON_OWNER:
                roles.add("ROLE_SALON_OWNER");
                break;
            // Add more cases for other user types if needed
            
            default:
                break;
        }
        
        return roles;
	}

}
