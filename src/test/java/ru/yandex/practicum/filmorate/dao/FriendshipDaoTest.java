package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.FriendshipDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@JdbcTest
@Sql({"classpath:/schema.sql", "classpath:/data.sql"})
@Import({FriendshipDaoImpl.class, UserDaoImpl.class})
public class FriendshipDaoTest {

    @Autowired
    private FriendshipDao friendshipDao;

    @Autowired
    private UserDao userDao;


    @Test
    public void shouldAddFriendship() {

        User user1 = userDao.saveUser(User.builder()
                .id(1L)
                .name("Oleg")
                .login("oleg_login")
                .email("oleg@test.ru")
                .birthday(LocalDate.now().minusYears(20))
                .build());

        User user2 = userDao.saveUser(User.builder()
                .id(2L)
                .name("Ivan")
                .login("ivan_login")
                .email("ivan@test.ru")
                .birthday(LocalDate.now().minusYears(30))
                .build());

        Friendship friendship = friendshipDao.saveFriendship(Friendship.builder()
                .userId(user1.getId())
                .friendId(user2.getId())
                .status(true)
                .build());

        assertNotNull(friendship);
    }

    @Test
    public void shouldDeleteFriendship() {

        User user1 = userDao.saveUser(User.builder()
                .id(1L)
                .name("Oleg")
                .login("oleg_login")
                .email("oleg@test.ru")
                .birthday(LocalDate.now().minusYears(20))
                .build());

        User user2 = userDao.saveUser(User.builder()
                .id(2L)
                .name("Ivan")
                .login("ivan_login")
                .email("ivan@test.ru")
                .birthday(LocalDate.now().minusYears(30))
                .build());

        Friendship friendship = friendshipDao.saveFriendship(Friendship.builder()
                .userId(user1.getId())
                .friendId(user2.getId())
                .status(true)
                .build());

        friendshipDao.deleteFriendship(friendship);

        var result = friendshipDao.findFriendship(friendship);

        assertFalse(result.isPresent());
    }

}
