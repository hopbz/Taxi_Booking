package com.taxi.repository;

import com.taxi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Optional<User> findByEmailAndRole(String email, String role);
    List<User> findByRole(String role);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
