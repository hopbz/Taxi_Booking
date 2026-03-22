package com.taxi.demo.dto;

public class UserResponseDTO {
    public Long id;
    public String email;
    public String fullName;
    public String role;
    public String phone;

    public UserResponseDTO() {
    }

    public UserResponseDTO(Long id, String email, String fullName, String role, String phone) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.phone = phone;
    }
}