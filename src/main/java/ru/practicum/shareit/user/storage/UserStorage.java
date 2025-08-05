package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

public interface UserStorage {
    User addOne(User user);

    User updateOne(long userId, User user);

    User getOne(long userId);

    void deleteOne(long userId);
}