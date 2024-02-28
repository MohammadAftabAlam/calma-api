package com.calmaapp.service;

import java.nio.file.attribute.UserPrincipal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.calmaapp.entity.User;
import com.calmaapp.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        // Load user details from the database based on the provided phone number
        User user = userRepository.findByPhoneNumber(phoneNumber);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with phone number: " + phoneNumber);
        }

        return user;
    }

    public UserDetails loadUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UsernameNotFoundException("User not found with id: " + userId);
        }
    }
}
