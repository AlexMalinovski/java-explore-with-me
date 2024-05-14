package ru.practicum.explorewithme.basic.service.users.services;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.api.NotFoundException;
import ru.practicum.explorewithme.basic.service.users.models.QUser;
import ru.practicum.explorewithme.basic.service.users.models.Role;
import ru.practicum.explorewithme.basic.service.users.models.User;
import ru.practicum.explorewithme.basic.service.users.repositories.RoleRepository;
import ru.practicum.explorewithme.basic.service.users.repositories.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @NonNull
    public List<User> getUsers(@Nullable List<Long> ids, int from, int size) {
        BooleanExpression filter = Expressions.TRUE.isTrue();
        if (ids != null) {
            filter = filter.and(QUser.user.id.in(ids));
        }
        return userRepository.findAllWithOffsetAndLimit(filter, from, size);
    }

    @Override
    @NonNull
    public User createUser(@NonNull User user) {
        if (user.getId() != null) {
            throw new IllegalArgumentException("id can be null.");
        }

        Role roleUser = roleRepository.findOneByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER was not exist"));
        User newUser = user.toBuilder()
                .roles(Set.of(roleUser))
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .build();
        return userRepository.save(newUser);
    }

    @Override
    public void deleteUser(long userId) {
        if (userId <= 0) {
            throw new NotFoundException(String.format("User with id='%s' was not found.", userId));
        }
        userRepository.deleteById(userId);
    }
}
