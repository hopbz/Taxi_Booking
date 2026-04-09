package com.taxi.controller;

import com.taxi.dto.RegisterUserRequest;
import com.taxi.dto.UserResponseDTO;
import com.taxi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody RegisterUserRequest request) {
        UserResponseDTO response = userService.registerNewUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}