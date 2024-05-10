package ru.practicum.ewm.auth.services.login;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import ru.practicum.ewm.auth.models.Role;
import ru.practicum.ewm.auth.models.User;
import ru.practicum.ewm.auth.repositories.UserRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> attr = mapProviderAttributes(userRequest);
        Set<SimpleGrantedAuthority> authority = userRepository.findOneByEmail((String) attr.get("email"))
                .map(User::getRoles)
                .orElseGet(() -> {
                    attr.put("isExist", false);
                    return Set.of(new Role(null, "ROLE_USER"));
                })
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
        attr.putIfAbsent("isExist", true);

        return new DefaultOAuth2User(authority, Collections.unmodifiableMap(attr), "email");
    }

    private Map<String, Object> mapProviderAttributes(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String clientRegId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attr = new HashMap<>();

        if ("yandex".equalsIgnoreCase(clientRegId)) {
            attr.put("name", oAuth2User.getAttribute("real_name"));
            attr.put("email", oAuth2User.getAttribute("default_email"));
        } else {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.SERVER_ERROR);
        }
        return attr;
    }
}
