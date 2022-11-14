package ru.yandex.practicum.filmorate.storage.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
@AllArgsConstructor
@Primary
public class DatabaseUserStorage implements UserStorage {
    private final UserDao userDao;
//    private final FriendshipDao friendshipDao;
//    private final EventsService eventsService;

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        userDao.saveUser(user);
        log.debug("Пользователь {} создан.", user.getName());
        return user;
    }

    @Override
    public User updateUser(User user) {
        findUserById(user.getId());
        userDao.updateUser(user);
        log.debug("Данные о пользователе {} обновлены.", user.getName());
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.findAllUsers();
    }

    @Override
    public User findUserById(Long id) {
        var user = userDao.findUserById(id);
        if (user.isPresent()) {
            return user.get();
        }
        throw new UserNotFoundException(
                String.format("Пользователь с таким id %s не существует", id));
    }
    //тест

    @Override
    public List<User> findAllUsersByIds(Set<Long> ids) {
        return userDao.findAllUserByIds(ids);
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> user = userDao.findUserById(id);
        if(user.isEmpty()){
            return;
        }
        userDao.deleteUser(user.get());

    }
}
