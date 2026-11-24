package com.example.shop.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AuthDto {

    @NotBlank(message = "El campo email esta vacio")
    @NotNull(message = "El campo email es obligatorio")
    private String email;

    @NotBlank(message = "El campo password esta vacio")
    @NotNull(message = "El campo password es obligatorio")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthDto{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
