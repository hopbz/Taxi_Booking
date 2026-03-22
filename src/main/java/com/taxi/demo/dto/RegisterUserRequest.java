package com.taxi.demo.dto;

public class RegisterUserRequest {
    public String email;
    public String fullName;
    public String password;
    public String role;
    public String phone;

    public RegisterUserRequest() {
    }

    public RegisterUserRequest(String email, String fullName, String password, String role, String phone) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.role = role;
        this.phone = phone;
    }
}
