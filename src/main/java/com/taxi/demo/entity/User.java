package com.taxi.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    // Constructor không tham số (bắt buộc cho JPA)
    public User() {
    }

    // Constructor có tham số (tiện lợi)
    public User(String email, String fullName, String password, String role, String phone) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.role = role;
        this.phone = phone;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Họ tên đầy đủ
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;
    @Column(nullable = false, unique = true, length = 120)
    private String email;
    @Column(nullable = false)
    private String password;
    // ROLE_PASSENGER, ROLE_DRIVER, ROLE_ADMIN
    @Column(nullable = false, length = 30)
    private String role;
    // Số điện thoại để tài xế / hành khách liên lạc
    private String phone;

    // Getter và Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
