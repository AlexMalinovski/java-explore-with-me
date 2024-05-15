package ru.practicum.ewm.auth.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.practicum.ewm.auth.models.Role;
import ru.practicum.ewm.auth.models.User;
import ru.practicum.ewm.auth.repositories.RoleRepository;
import ru.practicum.ewm.auth.repositories.UserRepository;
import ru.practicum.ewm.auth.services.login.CustomOAuth2UserService;
import ru.practicum.ewm.auth.services.login.FederatedIdentityAuthenticationSuccessHandler;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .userInfoEndpoint().userService(customOAuth2UserService()) // привести пользователя к типовому виду
                        .and().successHandler(federatedIdentityAuthenticationSuccessHandler()) // зарегистрировать если новый
                )
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    public FederatedIdentityAuthenticationSuccessHandler federatedIdentityAuthenticationSuccessHandler() {
        var handler = new FederatedIdentityAuthenticationSuccessHandler();
        handler.setOAuth2UserHandler(oAuth2User -> {
            Boolean isUserExist = (Boolean) Optional.ofNullable(oAuth2User.getAttribute("isExist"))
                    .orElseThrow(() -> new IllegalStateException("Required attribute 'isExist' is not present"));
            if (!isUserExist) {
                Set<Role> roles = roleRepository.findByNameIn(oAuth2User.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()));
                userRepository.save(User.builder()
                        .email(oAuth2User.getName())
                        .name(oAuth2User.getAttribute("name"))
                        .roles(roles)
                        .password(passwordEncoder().encode(UUID.randomUUID().toString())) // TODO сгенерировать случайный пароль и отправить письмо для его замены
                        .build());
            }
        });
        return handler;
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService(userRepository);
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepository.findOneByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));

            return org.springframework.security.core.userdetails.User
                    .builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .authorities(user.getRoles()
                            .stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .collect(Collectors.toSet()))
                    .build();
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}
