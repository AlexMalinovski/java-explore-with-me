package ru.practicum.explorewithme.basic.service.users.mappers;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.basic.lib.dto.users.NewUserDto;
import ru.practicum.explorewithme.basic.lib.dto.users.UserDto;
import ru.practicum.explorewithme.basic.lib.dto.users.UserShortDto;
import ru.practicum.explorewithme.basic.service.users.models.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapToUser(NewUserDto dto);

    UserDto mapToUserDto(User user);

    List<UserDto> mapToUserDto(List<User> users);

    UserShortDto mapToUserShortDto(User user);
}
