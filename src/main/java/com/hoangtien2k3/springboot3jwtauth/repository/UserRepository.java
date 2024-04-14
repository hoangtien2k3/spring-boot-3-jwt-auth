package com.hoangtien2k3.springboot3jwtauth.repository;

import com.hoangtien2k3.springboot3jwtauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
