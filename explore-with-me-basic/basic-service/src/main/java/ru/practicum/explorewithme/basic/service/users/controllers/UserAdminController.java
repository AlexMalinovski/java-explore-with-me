package ru.practicum.explorewithme.basic.service.users.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.basic.lib.dto.users.NewUserDto;
import ru.practicum.explorewithme.basic.lib.dto.users.UserDto;
import ru.practicum.explorewithme.basic.service.users.mappers.UserMapper;
import ru.practicum.explorewithme.basic.service.users.models.User;
import ru.practicum.explorewithme.basic.service.users.services.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/users")
@Validated
public class UserAdminController {

    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Возвращает информацию обо всех пользователях (учитываются параметры ограничения выборки),
     * либо о конкретных (учитываются указанные идентификаторы).
     * В случае, если по заданным фильтрам не найдено ни одного пользователя, возвращает пустой список.
     * Эндпоинт: GET /admin/users
     *
     * @param ids  id пользователей
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return List UserDto
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(required = false, defaultValue = "0") @Valid @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Valid @Positive int size) {

        List<User> users = userService.getUsers(ids, from, size);

        return ResponseEntity.ok(userMapper.mapToUserDto(users));
    }

    /**
     * Добавление нового пользователя.
     * Эндпоинт: POST /admin/users
     *
     * @param dto NewUserDto
     * @return UserDto
     */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid NewUserDto dto) {
        User user = userService.createUser(userMapper.mapToUser(dto));
        return new ResponseEntity<>(userMapper.mapToUserDto(user), HttpStatus.CREATED);
    }

    /**
     * Удаление пользователя.
     * Эндпоинт: DELETE /admin/users/{userId}
     *
     * @param userId id пользователя
     * @return no-body object
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
