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

    private final String MESSAGE ="message";
    private final String ROLE ="role";

    public RolesController(RoleRepository repo){
        this.repo = repo;
    }

    @GetMapping
    public  ResponseEntity<?> findAllRoles(){

        List<Role> roles =repo.findAll();
        Map<String,Object> response = new HashMap<>();
        if(roles.isEmpty()){
            response.put(MESSAGE,"no se encontraron roles");
            return ResponseEntity.badRequest().body(response);
        }
        return  ResponseEntity.ok(roles);


    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRole(@PathVariable Long id){
        Role role =repo.findById(id).orElse(null);
        Map<String,Object> response = new HashMap<>();
        if(role !=null)
        {
            response.put(ROLE,role);
            return ResponseEntity.ok(response);
        }

        response.put(MESSAGE,"Role no encontrado");
        return ResponseEntity.badRequest().body(response);

    }

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody @Valid  Role role){
        Map<String,Object> response = new HashMap<>();
        Role existRole = repo.findByName(role.getName()).orElse(null);
        if (existRole != null) {
            response.put("name", "El rol '" + existRole.getName() + "' ya existe");
            return ResponseEntity.badRequest().body(response);
        }

        Role savedRole = repo.save(role);
        response.put(MESSAGE,"Rol '" + savedRole.getName() + "' creado exitosamente");
        response.put(ROLE,savedRole);
        return ResponseEntity.ok(response);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody Role role){
        Map<String,Object> response = new HashMap<>();
        Role existRole = repo.findById(id).orElse(null);

        if(existRole != null){
            existRole.setName(role.getName());
            existRole.setDescription(role.getDescription());
            existRole.setUpdatedAt(LocalDateTime.now());
            repo.save(existRole);
            response.put(MESSAGE,"El rol fue actualizado");
            response.put(ROLE,existRole);
            return ResponseEntity.ok(response);
        }
        response.put(MESSAGE,"rol no encontrado");
        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id){
        Map<String,Object> response = new HashMap<>();
        Role existRole = repo.findById(id).orElse(null);
        if(existRole != null){
            repo.deleteById(id);
            response.put(MESSAGE,"El rol ha sido eliminado");
            return ResponseEntity.ok(response);

        }
        response.put(MESSAGE,"Rol no encontrado");
        return ResponseEntity.badRequest().body(response);

    }

}
