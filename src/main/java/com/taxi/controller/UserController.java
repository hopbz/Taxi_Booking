package com.taxi.controller;

import com.taxi.dto.RegisterUserRequest;
import com.taxi.dto.UserResponseDTO;
import com.taxi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody RegisterUserRequest request) {
        // Gọi Service xử lý logic và nhận về DTO
        UserResponseDTO responseBody = userService.registerNewUser(request);

        // Trả về HTTP Status 201 CREATED kèm theo body là UserResponseDTO
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }
}
