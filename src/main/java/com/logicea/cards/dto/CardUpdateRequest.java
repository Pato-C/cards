package com.logicea.cards.dto;

import com.logicea.cards.models.CardStatus;
import lombok.Data;

@Data
public class CardUpdateRequest {
    private String name;
    private String description;
    private String color;
    private CardStatus status;
}

