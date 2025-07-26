package com.example.shop.payloads;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RecoverPasswordDto {

    @NotBlank(message = "El campo password esta vacio")
    @NotNull(message = "El campo password es obligatorio")
    private String newPassword;

    @NotBlank(message = "El campo token esta vacio")
    @NotNull(message = "El campo token es obligatorio")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
