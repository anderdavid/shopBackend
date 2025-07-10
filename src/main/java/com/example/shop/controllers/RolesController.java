package com.example.shop.controllers;


import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/roles")
public class RolesController {
    public RolesController(){}

    @GetMapping
    public ResponseEntity<String> findAllRoles(){
        return ResponseEntity.ok("get all roles");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getRole(){
        return ResponseEntity.ok("get role");

    }

    @PostMapping
    public ResponseEntity<String> createRole(@RequestBody Objects o){
        return ResponseEntity.ok("get role");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String>updateRole(@PathVariable Long id, @RequestBody Objects o){
        return ResponseEntity.ok("actualizar role");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String>deleteRole(@PathVariable Long id){
        return ResponseEntity.ok("Eliminar role");
    }

}
