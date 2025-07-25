package com.example.shop.controllers;

import com.example.shop.models.Role;
import com.example.shop.models.User;
import com.example.shop.payloads.AuthDto;
import com.example.shop.payloads.ForgotPasswordDto;
import com.example.shop.payloads.RegisterUserDto;
import com.example.shop.repositories.RoleRepository;
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
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository repo;
    private final RoleRepository roleRepository;
    private final String MESSAGE ="message";
    private final String USER = "user";
    private final String URL = "url";
    private final String ROLE_USER ="ROLE_USER";
    private final String ACCESS_TOKEN ="access_token";
    private final Encrypt encrypt = new Encrypt();

    private JwtHelper jwtHelper;


    public AuthController(UserRepository repo, JwtHelper jwtHelper, RoleRepository roleRepository) {
        this.repo = repo;
        this.jwtHelper = jwtHelper;
        this.roleRepository = roleRepository;
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
        String token = jwtHelper.generateToken(mUser.getEmail(),mUser,jwtHelper.EXPIRATION_TIME);
        response.put(ACCESS_TOKEN,token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterUserDto mUser){
        Map<String, Object> response = new HashMap<>();
        User existUser = repo.findByEmail(mUser.getEmail()).orElse(null);
        if(existUser !=null){
            response.put(MESSAGE,"El correo "+mUser.getEmail()+" ya existe");
            return ResponseEntity.badRequest().body(response);
        }

        Role mRole = roleRepository.findByName(ROLE_USER).orElse(null);

        if(mRole ==null){
            response.put(MESSAGE,"El rol "+ROLE_USER+" no existe");
            return ResponseEntity.badRequest().body(response);
        }

        User newUser= new User();
        newUser.setFirstName(mUser.getFirstName());
        newUser.setLastName(mUser.getLastName());
        newUser.setAge(mUser.getAge());
        newUser.setEmail(mUser.getEmail());
        newUser.setPassword(encrypt.cryptPassword(mUser.getPassword()));
        newUser.getRoles().add(mRole);

        repo.save(newUser);
        response.put(MESSAGE,"usuario registrado");
        response.put(USER,newUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordDto data) throws Exception{
        HashMap<String,Object> response = new HashMap<>();
        User user = repo.findByEmail(data.getEmail()).orElse(null);
        if(user == null){
            response.put(MESSAGE,"No existe usuario con email "+data.getEmail()+"");
            return ResponseEntity.badRequest().body(response);
        }
        String token = jwtHelper.generateToken(user.getEmail(),user,jwtHelper.EXPIRATION_TIME_EMAIL_RECOVERY);
        String url ="https://miUrl?token="+token;
        response.put(URL,url);
        return ResponseEntity.ok(response);
    }
}
