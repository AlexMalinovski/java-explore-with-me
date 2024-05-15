package ru.practicum.ewm.auth.configs;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.practicum.ewm.auth.models.Role;
import ru.practicum.ewm.auth.models.User;
import ru.practicum.ewm.auth.repositories.RoleRepository;
import ru.practicum.ewm.auth.repositories.UserRepository;

import java.util.Set;

@Configuration
public class DbConfig {

    @Bean
    @Profile({"dev", "test"})
    ApplicationRunner initDb(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.deleteAll();
            roleRepository.deleteAll();

            Role roleUser = roleRepository.save(new Role(null, "ROLE_USER"));
            Role roleAdmin = roleRepository.save(new Role(null, "ROLE_ADMIN"));

            userRepository.save(User.builder()
                    .name("test")
                    .email("e@mail.ru")
                    .roles(Set.of(roleAdmin, roleUser))
                    .password(passwordEncoder.encode("password"))
                    .build());
        };
    }

    @Bean
    @Profile({"!dev", "!test"})
    ApplicationRunner initDbProd(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            Role roleUser = roleRepository.findOneByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER")));
            Role roleAdmin = roleRepository.findOneByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

            if (!userRepository.existsByEmail("e@mail.ru")) {
                userRepository.save(User.builder()
                        .name("admin")
                        .email("e@mail.ru")
                        .roles(Set.of(roleAdmin, roleUser))
                        .password(passwordEncoder.encode("admin"))
                        .build());
            }
        };
    }
}
