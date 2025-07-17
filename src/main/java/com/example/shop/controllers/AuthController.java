package com.example.shop.controllers;


import com.example.shop.payloads.AuthDto;
import com.example.shop.repositories.UserRepository;
import com.example.shop.utils.JwtHelper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository repo;
    private final String MESSAGE ="message";
    private final String USER = "user";

    private JwtHelper jwtHelper;

    public AuthController(UserRepository repo,JwtHelper jwtHelper) {
        this.repo = repo;
        this.jwtHelper = jwtHelper;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthDto auth) throws Exception {
        System.out.println("*************Test token***************");
        String mtoken = jwtHelper.generateToken("anderdavid","admin");
        System.out.println(mtoken);
        System.out.println("**************************************");
        return ResponseEntity.ok("login");
    }
}
