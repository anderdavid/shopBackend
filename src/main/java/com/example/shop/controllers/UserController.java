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
    public ResponseEntity<?> findAllUsers() {

        List<User> users = repo.findAll();
        Map<String,Object> response = new HashMap<>();
        if(users.isEmpty()){
            response.put(MESSAGE,"no se encontraron usuarios");
            return ResponseEntity.badRequest().body(response);
        }
        return  ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {

        User user = repo.findById(id).orElse(null);
        Map<String,Object> response = new HashMap<>();
        if(user == null){
            response.put(MESSAGE,"Usuario no encontrado");
            return ResponseEntity.badRequest().body(response);
        }
        response.put(USER,user);
        return ResponseEntity.ok(response);
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

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuario(@PathVariable Long id, @RequestBody @Valid UserDto mUser) {
        User existUser = repo.findById(id).orElse(null);
        Map<String,Object> response = new HashMap<>();

        if(existUser == null){
            response.put(MESSAGE,"Usuario no encontrado");
            return ResponseEntity.badRequest().body(response);
        }

        User otherUser = repo.findByEmail(mUser.getEmail()).orElse(null);
        if(otherUser !=null && otherUser.getId() != id){
            response.put(MESSAGE,"email en uso, use un email diferente");
            return ResponseEntity.badRequest().body(response);
        }

        List<String> roleNames = mUser.getRoles();
        List<Role> mRoles = new ArrayList<>();
        for(String roleName : roleNames){
            Role mRole = roleRepository.findByName(roleName).orElse(null);

            if(mRole ==null){
                response.put(MESSAGE,"El rol "+roleName+" no existe");
                return ResponseEntity.badRequest().body(response);
            }
            mRoles.add(mRole);
        }

            existUser.setFirstName(mUser.getFirstName());
            existUser.setLastName(mUser.getLastName());
            existUser.setAge(mUser.getAge());
            existUser.setEmail(mUser.getEmail());
            existUser.setPassword(mUser.getPassword());
            existUser.setUpdatedAt(LocalDateTime.now());
            mRoles.forEach(role->{
                existUser.getRoles().add(role);
            });
            response.put(MESSAGE,"usuario actualizado");
            response.put(USER,existUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>  deleteUser(@PathVariable Long id) {
        Map<String,Object> response = new HashMap<>();
        User existUser = repo.findById(id).orElse(null);

        if(existUser ==null){
            response.put(MESSAGE,"Usuario no encontrado");
            return ResponseEntity.badRequest().body(response);
        }
        repo.deleteById(id);
        response.put(MESSAGE,"El usuario '"+ existUser.getFirstName()+"' ha sido eliminado");
        return ResponseEntity.ok(response);
    }

}
