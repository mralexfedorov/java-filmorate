package ru.yandex.practicum.filmorate.dao;

import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.Events;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserDao {

    User saveUser(User user);

    User updateUser(User user);

    List<User> findAllUsers();

    Optional<User> findUserById(Long id);

    List<User> findAllUserByIds(Set<Long> ids);

    void deleteUser(User user);

}
