package com.hoangtien2k3.springboot3jwtauth.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Timestamps {
    LocalDateTime created_at;
    LocalDateTime updated_at;
}