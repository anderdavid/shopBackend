package com.example.shop.utils;

import com.example.shop.models.Role;
import com.example.shop.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;



import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class JwtHelper {

    private static final long EXPIRATION_TIME = 3600;
    private final String INFO ="info";
    private String secret;

    public JwtHelper(@Value("${JWT_SECRET}") String secret){
        this.secret = secret;
        System.out.println("JwtHelper()");
    }

    public String generateToken(String username, User user){
        Key key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA512");

        System.out.println("object user "+ user);

        HashMap<String,Object> aditionalInfo = new HashMap<>();
        aditionalInfo.put("firstname",user.getFirstName());
        aditionalInfo.put("lastName",user.getLastName());
        aditionalInfo.put("email",user.getEmail());

        List<String> roles = new ArrayList<>();

        user.getRoles().stream().forEach(
                role -> {
                    roles.add(role.getName());
                }
        );

        aditionalInfo.put("roles",roles);

        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, key)
                .setClaims(aditionalInfo)
                .setIssuer("echisan")
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME*1000))
                .compact();


        return token;

    }
}
