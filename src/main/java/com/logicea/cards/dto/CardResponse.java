package com.logicea.cards.dto;

import com.logicea.cards.models.CardStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CardResponse {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private String color;
    private CardStatus status;
    private LocalDateTime createdOn;
}
