package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.util.Fixtures.getUser;

@JdbcTest
@Sql({"classpath:/schema.sql"})
@Import(UserDaoImpl.class)
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void shouldCreateUser() {
        var user = userDao.saveUser(getUser());

        assertNotNull(user.getId());
    }

    @Test
    public void shouldUpdateUser() {
        var user = userDao.saveUser(getUser());
        user.setName("changed value name");
        user.setLogin("changed value login");
        user.setEmail("test2@test.ru");
        user.setBirthday(LocalDate.now().minusYears(50));

        var newUser = userDao.updateUser(user);

        assertEquals(user.getId(), newUser.getId());
        assertEquals(user.getLogin(), newUser.getLogin());
        assertEquals(user.getEmail(), newUser.getEmail());
        assertEquals(user.getBirthday(), newUser.getBirthday());
    }

    @Test
    public void shouldFindAllUsers() {
        userDao.saveUser(getUser());
        userDao.saveUser(getUser());
        userDao.saveUser(getUser());

        var result = userDao.findAllUsers();

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void shouldFindUserById() {
        var user = userDao.saveUser(getUser());

        var result = userDao.findUserById(user.getId());

        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
        assertEquals(user.getLogin(), result.get().getLogin());
        assertEquals(user.getEmail(), result.get().getEmail());
        assertEquals(user.getBirthday(), result.get().getBirthday());
    }

    @Test
    public void shouldFindAllUsersByIds() {
        User user = userDao.saveUser(getUser());
        User user1 = userDao.saveUser(getUser());
        User user2 = userDao.saveUser(getUser());

        Set<Long> ids = new HashSet<>();

        ids.add(user.getId());
        ids.add(user1.getId());
        ids.add(user2.getId());

        var result = userDao.findAllUserByIds(ids);

        assertNotNull(result);
        assertEquals(3, result.size());
    }
}
