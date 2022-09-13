package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private Integer userCount = 1;

    @PostMapping("/users")
    public User createUser(@RequestBody @Valid User user) {

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        user.setId(getAndIncrement(user));

        log.debug("Пользователь {} создан.", user.getName());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {

        if (users.get(user.getId()) == null) {
            throw new UserNotFoundException(
                    String.format("Пользователь с таким id %s не существует", user.getId()));
        }
        users.put(user.getId(), user);
        log.debug("Данные о пользователе {} обновлены.", user.getName());
        return user;
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {

        return new ArrayList<>(users.values());
    }

    private Integer getAndIncrement(User user) {
        if (user.getId() != null) {
            return user.getId();
        }
        return userCount++;
    }
}
