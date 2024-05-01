package com.calmaapp.payloads;

import com.calmaapp.UserType;
import com.calmaapp.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String phoneNumber;
    private UserType userType; 
    private String name;
    private String password;
	private String email;
    private int age;
    private String location;
    private String gender;
	private String jwtToken;
	
	
	
	public UserDTO(String phoneNumber, UserType userType, String name, String password, String email,int age, String location,
			String gender, String jwtToken) {
		super();
		this.phoneNumber = phoneNumber;
		this.userType = userType;
		this.name = name;
		this.password = password;
		this.email=email;
		this.age = age;
		this.location = location;
		this.gender = gender;
		this.jwtToken = jwtToken;
	}

	
	public UserDTO(User user) {
		this.phoneNumber = user.getPhoneNumber();
		this.userType = user.getUserType();
		this.name = user.getName();
		this.password=user.getPassword();
		this.age = user.getAge();
		this.location = user.getLocation();
		this.gender = user.getGender();
	}

   
}
