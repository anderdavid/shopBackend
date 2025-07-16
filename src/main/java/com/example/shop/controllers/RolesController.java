package com.example.shop.controllers;

import com.example.shop.models.Role;
import com.example.shop.repositories.RoleRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/roles")
public class RolesController {
    private final RoleRepository repo;
    public RolesController(RoleRepository repo){
        this.repo = repo;
    }

    @GetMapping
    public  List<Role>findAllRoles(){
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Role getRole(@PathVariable Long id){
        return repo.findById(id).orElse(null);

    }

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody @Valid  Role role){
        Role existRole = repo.findByName(role.getName()).orElse(null);
        if (existRole != null) {
            Map<String, String> error = new HashMap<>();
            error.put("name", "El rol '" + existRole.getName() + "' ya existe");
            return ResponseEntity.badRequest().body(error);
        }

        Role savedRole = repo.save(role);
        return ResponseEntity.ok("Rol '" + savedRole.getName() + "' creado exitosamente");

    }

    @PutMapping("/{id}")
    public Role updateRole(@PathVariable Long id, @RequestBody Role role){
        Role existRole = repo.findById(id).orElse(null);

        if(existRole != null){
            existRole.setName(role.getName());
            existRole.setDescription(role.getDescription());
            existRole.setUpdatedAt(LocalDateTime.now());

            repo.save(existRole);
        }
        return null;
    }
    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id){
        repo.deleteById(id);
    }

}
