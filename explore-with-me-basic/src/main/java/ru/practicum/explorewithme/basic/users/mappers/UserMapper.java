package ru.practicum.explorewithme.basic.users.mappers;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.basic.users.dto.NewUserDto;
import ru.practicum.explorewithme.basic.users.dto.UserDto;
import ru.practicum.explorewithme.basic.users.dto.UserShortDto;
import ru.practicum.explorewithme.basic.users.models.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapToUser(NewUserDto dto);

    UserDto mapToUserDto(User user);

    List<UserDto> mapToUserDto(List<User> users);

    UserShortDto mapToUserShortDto(User user);
}
