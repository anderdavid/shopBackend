package com.example.shop.utils;

import com.example.shop.models.Role;
import com.example.shop.models.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
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
    private static String secret;

    public JwtHelper(@Value("${JWT_SECRET}") String secret){
        this.secret = secret;
        System.out.println("JwtHelper()");
    }

    /*public String generateToken(String username, User user){
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
                .setIssuer("")
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME*1000))
                .compact();


        return token;

    }*/

    public String generateToken(String username,User user) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA512");

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

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("username")
                .issuer("shop")
                .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION_TIME*1000))
                .claim("aditional info",aditionalInfo)
                .build();

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        JWSSigner signer = new MACSigner(secretKey);
        signedJWT.sign(signer);

        String token = signedJWT.serialize();

        return token;
    }
}
