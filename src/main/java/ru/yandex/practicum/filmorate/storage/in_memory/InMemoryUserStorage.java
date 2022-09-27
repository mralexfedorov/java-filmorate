package ru.yandex.practicum.filmorate.storage.in_memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users = new HashMap<>();

    private Long userCount = 1L;

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        user.setId(getAndIncrement(user));

        log.debug("Пользователь {} создан.", user.getName());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        findUser(user.getId());
        users.put(user.getId(), user);
        log.debug("Данные о пользователе {} обновлены.", user.getName());
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public List<User> findAllUsersByIds(Set<Long> ids) {
        List<User> result = new ArrayList<>();
        for (Long id : ids) {
            var user = findUser(id);
            result.add(user);
        }
        return result;
    }

    @Override
    public User findUser(Long id) {
        var user = users.get(id);
        if (user != null) {
            return user;
        }
        throw new UserNotFoundException(
                String.format("Пользователь с таким id %s не существует", id));
    }


    private Long getAndIncrement(User user) {
        if (user.getId() != null) {
            return user.getId();
        }
        return userCount++;
    }
}
