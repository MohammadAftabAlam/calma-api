package com.calmaapp.authentication;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LogoutRequest {
    private String token;

    // Constructors, getters, and setters
    // Ensure appropriate constructors, getters, and setters for all fields
    // For example:
    public LogoutRequest(String token) {
        this.token = token;
    }
    // Generate other constructors, getters, and setters as needed
}

