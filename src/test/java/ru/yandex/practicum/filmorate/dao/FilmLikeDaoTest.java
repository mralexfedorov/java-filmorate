package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.FilmLikeDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.yandex.practicum.filmorate.util.Fixtures.getFilm;
import static ru.yandex.practicum.filmorate.util.Fixtures.getUser;

@JdbcTest
@Sql({"classpath:/schema.sql", "classpath:/data.sql"})
@Import({FilmLikeDaoImpl.class, FilmDaoImpl.class, UserDaoImpl.class})
public class FilmLikeDaoTest {
    @Autowired
    private FilmLikeDao filmLikeDao;
    @Autowired
    private FilmDao filmDao;
    @Autowired
    private UserDao userDao;

    @Test
    public void shouldAddLike() {

        User user = userDao.saveUser(getUser());

        Film film = filmDao.saveFilm(getFilm());

        FilmLike filmLike = filmLikeDao.saveLike(new FilmLike(1l, user.getId(), film.getId()));

        assertNotNull(filmLike);

    }

    @Test
    public void shouldFindLikesByFilmId() {

        User user = userDao.saveUser(getUser());
        User user1 = userDao.saveUser(getUser());
        User user2 = userDao.saveUser(getUser());

        Film film = filmDao.saveFilm(getFilm());

        FilmLike filmLike = filmLikeDao.saveLike(new FilmLike(1l, user.getId(), film.getId()));
        FilmLike filmLike1 = filmLikeDao.saveLike(new FilmLike(2l, user1.getId(), film.getId()));
        FilmLike filmLike2 = filmLikeDao.saveLike(new FilmLike(3l, user2.getId(), film.getId()));

        List<FilmLike> likesOfFilm = filmLikeDao.getFilmLikes(film.getId());

        assertEquals(3, likesOfFilm.size());

    }

    @Test
    public void shouldDeleteLike() {

        User user = userDao.saveUser(getUser());

        Film film = filmDao.saveFilm(getFilm());

        FilmLike filmLike = filmLikeDao.saveLike(FilmLike.builder()
                .filmId(film.getId())
                .userId(user.getId())
                .build());

        filmLikeDao.deleteLike(filmLike);

        List<FilmLike> result = filmLikeDao.getFilmLikes(film.getId());

        assertEquals(0, result.size());
    }
}
