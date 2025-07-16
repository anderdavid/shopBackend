package com.example.shop.payloads;

import jakarta.validation.constraints.*;

public class UserDto {

    @NotNull(message = "El campo firstName es obligatorio")
    @Size(min = 2, message = "El campo firstName debe tener al menos 2 caracteres")
    private String firstName;

    @NotNull(message = "El campo LastName es obligatorio")
    @Size(min = 2, message = "El campo LastName debe tener al menos 2 caracteres")
    private String lastName;

    @NotNull(message = "El campo age es obligatorio")
    private Long age;

    @NotNull(message = "El campo email es obligatorio")
    @Email(message = "Direccion email no valida")
    private String email;

    @NotNull(message = "El campo password es obligatorio")
    @Size(min = 8, message = "El campo password debe tener al menos 8 caracteres")
    private String password;

    @NotNull(message = "El campo role es obligatorio")
    private String role;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
