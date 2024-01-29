package ru.practicum.explorewithme.basic.users.services;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.practicum.explorewithme.basic.users.models.User;

import java.util.List;

public interface UserService {

    @NonNull
    List<User> getUsers(@Nullable List<Long> ids, int from, int size);

    @NonNull
    User createUser(@NonNull User user);

    void deleteUser(long userId);

}
