package com.logicea.cards.entity;

import com.logicea.cards.models.CardStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name = "cards")
@Data
public class CardsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "color")
    private String color;

    @Enumerated(EnumType.STRING)
    @Column()
    private CardStatus status;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

}

