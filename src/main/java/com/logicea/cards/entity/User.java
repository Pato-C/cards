package com.logicea.cards.entity;


import com.logicea.cards.dto.UserRole;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "created_on", nullable = false, updatable = false)
    private Timestamp createdOn;

    @Column(name = "updated_on", nullable = false)
    private Timestamp updatedOn;

    // Constructors, getters, and setters

    // Default constructor for JPA
    public User() {
    }

    public User(String email, String password, UserRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and setters
}
