package com.example.shop.controllers;

import com.example.shop.models.User;
import com.example.shop.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<String> findAllUsers(){
        //return repo.findAll();
        return ResponseEntity.ok("Lista de usuarios obtenida correctamente.");
    }
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id){
        return repo.findById(id).orElse(null);
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        return repo.save(user);
    }

    @PutMapping("/{id}")
    public User updateUsuario(@PathVariable Long id, @RequestBody User user){
        User existUser = repo.findById(id).orElse(null);
        if(existUser != null){
            existUser.setNombre(user.getNombre());
            existUser.setEdad(user.getEdad());
            repo.save(existUser);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        repo.deleteById(id);
    }

}
