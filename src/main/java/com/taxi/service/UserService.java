package com.taxi.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.taxi.dto.RegisterUserRequest;
import com.taxi.dto.UserResponseDTO;
import com.taxi.dto.auth.LoginRequest;
import com.taxi.dto.auth.LoginResponse;
import com.taxi.entity.User;
import com.taxi.mapper.UserMapper;
import com.taxi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDTO registerNewUser(RegisterUserRequest request) {
        // TODO: kiểm tra email trùng
        // TODO: set role mặc định nếu không truyền
        // TODO: mã hoá password spring security
        User user = userMapper.toEntity(request);

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("PASSENGER");
        }

        User savedUser = userRepository.save(user);
        return userMapper.toResponseDTO(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        // TODO: thay bằng logic login hiện tại của dự án

        throw new UnsupportedOperationException("Implement login logic here");
    }

    public UserResponseDTO getMe(Authentication authentication) {
        // Nếu authentication.getName() là email/username
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toResponseDTO(user);
    }
}