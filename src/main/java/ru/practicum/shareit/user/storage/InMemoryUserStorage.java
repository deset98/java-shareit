package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Repository("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long userId;

    @Override
    public User addOne(User user) {
        this.checkExistsEmail(user.getEmail());
        this.checkNotNullEmail(user.getEmail());

        user.setId(++userId);
        users.put(userId, user);
        return users.get(userId);
    }

    @Override
    public User updateOne(long userId, User newUser) {
        this.checkExistsEmail(newUser.getEmail());

        User oldUser = users.get(userId);
        if (newUser.getName() != null && !newUser.getName().isBlank()) {
            oldUser.setName(newUser.getName());
        }
        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {
            oldUser.setEmail(newUser.getEmail());
        }
        return users.get(userId);
    }

    @Override
    public User getOne(long userId) {
        return users.get(userId);
    }

    @Override
    public void deleteOne(long userId) {
        users.remove(userId);
    }

    private void checkExistsEmail(String email) {
        if (users.values().stream().anyMatch(user -> user.getEmail().equals(email))) {
            throw new DuplicateException("Такой email = {}, уже зарегистрирован", email);
        }
    }

    private void checkNotNullEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new NullPointerException("Поле email не заполнено");
        }
    }
}