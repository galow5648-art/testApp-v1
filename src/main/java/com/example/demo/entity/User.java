package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    String email;
    String passwordHash;

    @Enumerated(EnumType.STRING)
    Role role;

    public enum Role {
        ADMIN, USER
    }
}