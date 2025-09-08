package com.example.shop.config;

import com.example.shop.models.Role;
import com.example.shop.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInicializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInicializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        String adminRole = "ROLE_ADMIN";
        String descriptionRole = "usuario administrador";

        if(roleRepository.findByName(adminRole).isEmpty()){
            Role role = new Role();
            role.setName(adminRole);
            role.setDescription(descriptionRole);
            roleRepository.save(role);
            System.out.println("El rol "+adminRole+"ha sido creado");
        }else{
            System.out.println("El rol "+adminRole+"ha sido creado");
        }
    }
}
