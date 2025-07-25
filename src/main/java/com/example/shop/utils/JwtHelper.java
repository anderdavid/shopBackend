package com.example.shop.utils;


import com.example.shop.models.User;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class JwtHelper {

    public static final long EXPIRATION_TIME = 3600;
    public static final long EXPIRATION_TIME_EMAIL_RECOVERY = 300;
    private final String INFO ="info";
    private static String secret;

    public JwtHelper(@Value("${JWT_SECRET}") String secret){
        this.secret = secret;
        System.out.println("JwtHelper()");
    }

    public String generateToken(String username,User user,long expirationTime) throws Exception {

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

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("username")
                .issuer("shop")
                .expirationTime(new Date(System.currentTimeMillis() + expirationTime*1000))
                .claim("roles",roles)
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
