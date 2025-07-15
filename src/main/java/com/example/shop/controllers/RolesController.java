package com.example.shop.controllers;


import com.example.shop.models.Role;
import com.example.shop.repositories.RoleRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    public Role createRole(@RequestBody Role role){
        return repo.save(role);
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
