package com.example.shop.controllers;


import com.example.shop.models.User;
import com.example.shop.payloads.AuthDto;
import com.example.shop.repositories.UserRepository;
import com.example.shop.utils.Encrypt;
import com.example.shop.utils.JwtHelper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository repo;
    private final String MESSAGE ="message";
    private final String ACCESS_TOKEN ="access_token";

    private JwtHelper jwtHelper;


    public AuthController(UserRepository repo,JwtHelper jwtHelper) {
        this.repo = repo;
        this.jwtHelper = jwtHelper;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthDto auth) throws Exception {
        User mUser = repo.findByEmail(auth.getEmail()).orElse(null);

        HashMap<String,Object> response = new HashMap<>();
        if(mUser == null){
            response.put(MESSAGE,"El usuario con email "+auth.getEmail()+" no existe");
            return ResponseEntity.badRequest().body(response);
        }

        Encrypt encrypt = new Encrypt();
        boolean match = encrypt.comparePassword(auth.getPassword(), mUser.getPassword());

        if(!match){
            response.put(MESSAGE,"La contraseña es incorrecta");
            return ResponseEntity.badRequest().body(response);
        }
        String token = jwtHelper.generateToken(mUser.getEmail(),mUser);
        response.put(ACCESS_TOKEN,token);

        return ResponseEntity.ok(response);
    }
}
