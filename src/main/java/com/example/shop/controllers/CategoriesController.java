package com.example.shop.controllers;


import com.example.shop.models.Categories;
import com.example.shop.payloads.CategoriesDto;
import com.example.shop.repositories.CategoriesRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoriesController {
    private final CategoriesRepository repo;

    private final String MESSAGE = "message";
    private final String CATEGORY ="category";

    public CategoriesController(CategoriesRepository repo){
        this.repo = repo;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllCategories(){
        Map<String,Object> response = new HashMap<>();
        List<Categories> categories = repo.findAll();
        if(categories.isEmpty()){
            response.put(MESSAGE,"No se encotraron categorias");
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(categories);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable Long id){
        Map<String,Object> response = new HashMap<>();
        Categories category = repo.findById(id).orElse(null);
        if(category == null){
            response.put(MESSAGE,"Categoria no encontrada");
            return ResponseEntity.badRequest().body(response);
        }
        response.put(CATEGORY,category);
        return ResponseEntity.ok(response);

    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoriesDto category){
        Map<String,Object> response = new HashMap<>();
        Categories existCategory = repo.findByName(category.getName()).orElse(null);

        if(existCategory != null){
            response.put(MESSAGE, "La categoria "+category.getName()+" ya existe");
            return ResponseEntity.badRequest().body(MESSAGE);
        }
        Categories mCategory = new Categories();
        mCategory.setName(category.getName());
        mCategory.setDescription(category.getDescription());
        repo.save(mCategory);
        response.put(MESSAGE,"La categoria '"+category.getName()+"' creada exitosamente");
        response.put(CATEGORY,mCategory);
        return ResponseEntity.ok(response);

    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id,@RequestBody @Valid CategoriesDto category){
        Map<String,Object> response = new HashMap<>();

        Categories existCategory = repo.findById(id).orElse(null);
        if(existCategory == null){
            response.put(MESSAGE,"Categoria no encontrada");
            return ResponseEntity.badRequest().body(response);
        }

        existCategory.setName(category.getName());
        existCategory.setDescription(category.getDescription());
        repo.save(existCategory);

        response.put(MESSAGE,"La categoria '"+existCategory.getName()+"' ha sido actualizada");
        response.put(CATEGORY,existCategory);
        return ResponseEntity.ok(response);

    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        Map<String,Object> response = new HashMap<>();

        Categories category = repo.findById(id).orElse(null);
        if(category == null){
            response.put(MESSAGE,"Categoria no encotrada");
            return ResponseEntity.badRequest().body(response);
        }
        repo.deleteById(id);
        response.put(MESSAGE,"La categoria ha sido eliminada");
        return ResponseEntity.ok(response);
    }
}
