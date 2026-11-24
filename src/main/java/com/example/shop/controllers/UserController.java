package com.example.shop.controllers;

import com.example.shop.models.Role;
import com.example.shop.models.User;
import com.example.shop.payloads.UserDto;
import com.example.shop.repositories.RoleRepository;
import com.example.shop.repositories.UserRepository;
import com.example.shop.utils.Encrypt;
import com.example.shop.utils.JwtHelper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository repo;
    private final RoleRepository roleRepository;
    private JwtHelper jwtHelper;

    private final String MESSAGE ="message";
    private final String USER = "user";
    private final Encrypt encrypt = new Encrypt();

    public UserController(UserRepository repo,RoleRepository roleRepository,JwtHelper jwtHelper) {
        this.repo = repo;
        this.roleRepository = roleRepository;
        this.jwtHelper = jwtHelper;
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String header){
        Map<String,Object> response = new HashMap<>();
        String token = header.replace("Bearer ","");
        String email = jwtHelper.getEmailByToken(token);
        User me  = repo.findByEmail(email).orElse(null);
        if(me == null){
            response.put(MESSAGE,"Error obteniendo usuario");
            ResponseEntity.badRequest().body(response);
        }
        response.put(USER,me);
        return ResponseEntity.ok(response);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
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

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
        newUser.setPassword(encrypt.cryptPassword(mUser.getPassword()));

        mRoles.forEach(role->{
            newUser.getRoles().add(role);
        });

        repo.save(newUser);

        response.put(MESSAGE,"usuario creado");
        response.put(USER,newUser);
        return ResponseEntity.ok(response);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
            if(mUser.getPassword()!=null){
                existUser.setPassword(encrypt.cryptPassword(mUser.getPassword()));
            }
            existUser.setUpdatedAt(LocalDateTime.now());
            mRoles.forEach(role->{
                existUser.getRoles().add(role);
            });
            repo.save(existUser);
            response.put(MESSAGE,"usuario actualizado");
            response.put(USER,existUser);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
