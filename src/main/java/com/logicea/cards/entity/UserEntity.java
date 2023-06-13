package com.logicea.cards.entity;


import com.logicea.cards.models.UserRole;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Data
public class UserEntity {

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

    public UserEntity() {
    }

    public UserEntity(String email, String password, UserRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
