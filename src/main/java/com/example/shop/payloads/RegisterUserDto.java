package com.example.shop.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
public class RegisterUserDto {

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

    private String password;


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


}

