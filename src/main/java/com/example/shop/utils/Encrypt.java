package com.example.shop.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encrypt {

    public String cryptPassword (String password){
         BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
         return encoder.encode(password);
    }

    public boolean comparePassword(String password,String encryptPassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password,encryptPassword);
    }
}
