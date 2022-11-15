package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.FilmGenreDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.GenreDaoImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.util.Fixtures.getFilm;


@JdbcTest
@Sql({"classpath:/schema.sql", "classpath:/data.sql"})
@Import({GenreDaoImpl.class, FilmDaoImpl.class, FilmGenreDaoImpl.class})
public class GenreDaoTest {

    @Autowired
    private GenreDao genreDao;

    @Autowired
    private FilmDao filmDao;

    @Autowired
    private FilmGenreDao filmGenreDao;

    @Test
    public void shouldFindAllGenres() {

        List<Genre> result = genreDao.findAllGenres();

        assertNotNull(result);
        assertEquals(6, result.size());
    }

    @Test
    public void shouldFindGenreById() {

        Optional<Genre> genre = genreDao.findGenreById(3l);

        assertNotNull(genre);
        assertEquals("Мультфильм", genre.get().getName());
    }

    @Test
    public void shouldFindGenreByFilmId() {

        Film film = filmDao.saveFilm(getFilm());
        filmGenreDao.linkGenreToFilm(film.getId(), 3L);

        List<Genre> genre = genreDao.findGenreByFilmId(film.getId());

        assertNotNull(genre);
        assertEquals("Мультфильм", genre.get(0).getName());
    }

    @Test
    public void shouldDeleteFilmGenres() {

        Film film = filmDao.saveFilm(getFilm());

        filmGenreDao.linkGenreToFilm(film.getId(), 3L);

        filmGenreDao.deleteFilmGenres(film.getId());

        boolean result = filmGenreDao.linkAlreadyExist(film.getId(), 3L);

        assertFalse(result);
    }

}
