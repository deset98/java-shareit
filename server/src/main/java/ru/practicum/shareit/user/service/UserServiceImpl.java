package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

@Service("UserServiceImpl")
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(@Qualifier("UserJpaRepository") final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        if (!userRepository.existsByEmail(userDto.getEmail())) {
            User user = userRepository.save(UserMapper.toUser((userDto)));
            return UserMapper.toUserDto(user);
        }
        throw new DuplicateException("Пользователь с Email: {} уже существует", userDto.getEmail());
    }

    @Override
    public UserDto getUser(long userId) {
        User user = userRepository
                .findById(userId).orElseThrow(() -> new NotFoundException("User с id={} не существует", userId));
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("такого пользователя нет"));
        if (userDto.getEmail() != null) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new DuplicateException("Пользователь с Email: {} уже существует", userDto.getEmail());
            }
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }
}