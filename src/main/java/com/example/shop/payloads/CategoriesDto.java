package com.example.shop.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoriesDto {

    @NotBlank(message= "El campo name esta vacio")
    @NotNull(message= "El campo name es obligatorio")
    private String name;

    @NotBlank(message = "El campo description esta vacio")
    @NotNull(message = "El campo description es obligatorio")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
