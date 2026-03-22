package com.taxi.demo.service;

import com.taxi.demo.dto.RegisterUserRequest;
import com.taxi.demo.dto.UserResponseDTO;
import com.taxi.demo.entity.User;
import com.taxi.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.email)) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        User user = new User();
        user.setEmail(request.email);
        user.setFullName(request.fullName);
        user.setPassword(passwordEncoder.encode(request.password)); // Mã hóa password
        user.setRole(request.role);
        user.setPhone(request.phone);

        User saved = userRepository.save(user);
        return mapToDTO(saved);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(@NonNull Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user với id = " + id));
        return mapToDTO(user);
    }

    public UserResponseDTO updatePhone(@NonNull Long id, String phone) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user với id = " + id));

        user.setPhone(phone);
        User updated = userRepository.save(user);
        return mapToDTO(updated);
    }

    public void deleteUser(@NonNull Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy user với id = " + id);
        }
        userRepository.deleteById(id);
    }

    private UserResponseDTO mapToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.id = user.getId();
        dto.email = user.getEmail();
        dto.fullName = user.getFullName();
        dto.role = user.getRole();
        dto.phone = user.getPhone();
        return dto;
    }
}
