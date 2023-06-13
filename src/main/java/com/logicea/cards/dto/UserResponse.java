package com.logicea.cards.dto;

import lombok.Data;

@Data
public class UserResponse {
    String email;
    String  role;
    String token;
}
