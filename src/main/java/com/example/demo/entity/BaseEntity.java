package com.example.demo.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    LocalDateTime createAt;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
    }
}