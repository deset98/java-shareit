package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    UserDto addUser(UserDto userDto);

    UserDto updateUser(long userId, UserDto userDto);

    UserDto getUser(long userId);

    void deleteUser(long userId);
}