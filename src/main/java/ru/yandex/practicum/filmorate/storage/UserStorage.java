package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    List<User> findAllUsers();

    List<User> findAllUsersByIds(Set<Long> ids);

    User findUser(Long id);

}
