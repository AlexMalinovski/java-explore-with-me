package ru.practicum.ewm.auth.configs;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.practicum.ewm.auth.models.User;
import ru.practicum.ewm.auth.repositories.UserRepository;

@Configuration
public class DbConfig {

    @Bean
    @Profile({"dev","test"})
    ApplicationRunner initDb(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.save(User.builder()
                    .name("test")
                    .email("e@mail.ru")
                    .password(passwordEncoder.encode("password"))
                    .build());
        };
    }
}
