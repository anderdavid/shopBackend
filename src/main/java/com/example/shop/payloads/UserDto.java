package com.example.shop.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class UserDto extends RegisterUserDto {

    @NotNull(message = "La lista de roles no puede ser nula")
    @Size(min = 1, message = "Debe haber al menos un rol")
    private List<@NotBlank(message = "El nombre del rol no puede estar vacío") String> roles;

    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
