package com.example.shop.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ForgotPasswordDto {

    @NotBlank(message = "El campo email esta vacio")
    @NotNull(message = "El campo email es obligatorio")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
