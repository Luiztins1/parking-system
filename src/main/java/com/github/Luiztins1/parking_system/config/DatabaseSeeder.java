package com.github.Luiztins1.parking_system.config;

import com.github.Luiztins1.parking_system.model.entity.UserAuth;
import com.github.Luiztins1.parking_system.repository.UserAuthRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DatabaseSeeder {

    @Bean
    public CommandLineRunner commandLineRunner(UserAuthRepository repository, PasswordEncoder passwordEncoder){
        return args ->{
            if(repository.count() == 0){
                var userAuthAdmin = new UserAuth();
                userAuthAdmin.setLogin("admin");
                userAuthAdmin.setPassword(passwordEncoder.encode("admin123"));
                userAuthAdmin.setRoles(List.of("ADMIN"));

                repository.save(userAuthAdmin);
            }
        };
    }
}
