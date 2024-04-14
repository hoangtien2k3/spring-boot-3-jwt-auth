package com.hoangtien2k3.springboot3jwtauth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String email;
    String password;
    @ElementCollection
    Set<String> roles;
    @Embedded
    Timestamps timestamps;
}
