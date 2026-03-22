package com.taxi.demo.controller;

import com.taxi.demo.dto.RegisterUserRequest;
import com.taxi.demo.dto.UserResponseDTO;
import com.taxi.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public UserResponseDTO createUser(@RequestBody RegisterUserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable @NonNull Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/phone")
    public UserResponseDTO updatePhone(@PathVariable @NonNull Long id, @RequestParam String phone) {
        return userService.updatePhone(id, phone);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable @NonNull Long id) {
        userService.deleteUser(id);
        return "Xóa user thành công";
    }
}