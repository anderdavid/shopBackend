package com.example.shop.repositories;

import com.example.shop.models.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Categories,Long> {
    Optional<Categories> findByName(String name);
}
