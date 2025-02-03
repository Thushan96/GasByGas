package com.esoft.gascollect.controller;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

    // Getters and Setters
    @NotNull(message = "email cannot be null")
    private String email;

    @NotNull(message = "Password cannot be null")
    private String password;

}
