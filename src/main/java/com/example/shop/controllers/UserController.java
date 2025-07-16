package com.example.shop.controllers;

import com.example.shop.models.Role;
import com.example.shop.models.User;
import com.example.shop.payloads.UserDto;
import com.example.shop.repositories.RoleRepository;
import com.example.shop.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.System.in;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository repo;
    private final RoleRepository roleRepository;
    private final String MESSAGE ="message";
    private final String USER = "user";

    public UserController(UserRepository repo,RoleRepository roleRepository) {
        this.repo = repo;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public List<User> findAllUsers() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDto mUser) {
        Map<String, Object> response = new HashMap<>();
        User existUser = repo.findByEmail(mUser.getEmail()).orElse(null);
        if(existUser !=null){
            response.put(MESSAGE,"El correo "+mUser.getEmail()+" ya existe");
            return ResponseEntity.badRequest().body(response);
        }

        List<String> roleNames = mUser.getRoles();
        System.out.println("rolesNames "+roleNames);
        List<Role> mRoles = new ArrayList<>();
        for(String roleName : roleNames){
            Role mRole = roleRepository.findByName(roleName).orElse(null);

            if(mRole ==null){
                response.put(MESSAGE,"El rol "+roleName+" no existe");
                return ResponseEntity.badRequest().body(response);
            }
            mRoles.add(mRole);
        }

        User newUser= new User();
        newUser.setFirstName(mUser.getFirstName());
        newUser.setLastName(mUser.getLastName());
        newUser.setAge(mUser.getAge());
        newUser.setEmail(mUser.getEmail());
        newUser.setPassword(mUser.getPassword());

        mRoles.forEach(role->{
            newUser.getRoles().add(role);
        });

        repo.save(newUser);

        response.put(MESSAGE,"usuario creado");
        response.put(USER,newUser);
        return ResponseEntity.ok(response);
        //return ResponseEntity.ok("validando...");

    }

    @PutMapping("/{id}")
    public User updateUsuario(@PathVariable Long id, @RequestBody User user) {
        User existUser = repo.findById(id).orElse(null);
        if (existUser != null) {
            existUser.setFirstName(user.getFirstName());
            existUser.setLastName(user.getLastName());
            existUser.setEmail(user.getEmail());
            existUser.setAge(user.getAge());
            existUser.setUpdatedAt(LocalDateTime.now());
            repo.save(existUser);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        repo.deleteById(id);
    }

}
