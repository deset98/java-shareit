package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public UserServiceImpl(@Qualifier("InMemoryUserStorage") final UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userStorage.addOne(user));
    }

    @Override
    public UserDto getUser(long userId) {
        return UserMapper.toUserDto(userStorage.getOne(userId));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userStorage.updateOne(userId, user));
    }

    @Override
    public void deleteUser(long userId) {
        userStorage.deleteOne(userId);
    }
}