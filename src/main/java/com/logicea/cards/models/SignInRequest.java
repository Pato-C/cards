package com.logicea.cards.models;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    private String password;
}
