package com.logicea.cards.dto;

import com.logicea.cards.models.CardStatus;
import lombok.Data;

import java.time.LocalDate;
@Data
public class CardSearchCriteria {
    private String name;
    private long userId;
    private String color;
    private CardStatus status;
    private LocalDate creationDate;
    private int page;
    private int size;
}
