package com.taxi.service;

import com.taxi.dto.RegisterUserRequest;
import com.taxi.dto.UserResponseDTO;
import com.taxi.dto.auth.LoginRequest;
import com.taxi.dto.auth.LoginResponse;
import com.taxi.entity.User;
import com.taxi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO registerNewUser(RegisterUserRequest request) {
        // 1. Map DTO (RegisterUserRequest) sang Entity (User)
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        // 2. Lưu vào DB
        User savedUser = userRepository.save(user);

        // 3. Map Entity (User) sang DTO (UserResponseDTO)
        return toUserResponseDTO(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        // TODO: Implement login logic with authentication
        return new LoginResponse();
    }

    public UserResponseDTO getMe(Authentication authentication) {
        // TODO: Implement getMe logic using authentication
        return new UserResponseDTO();
    }

    private UserResponseDTO toUserResponseDTO(User user) {
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setFullName(user.getFullName());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setPhone(user.getPhone());
        responseDTO.setRole(user.getRole());

        return responseDTO;
    }
}
