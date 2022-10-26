package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.util.Fixtures.getFilm;

@JdbcTest
@Sql({"classpath:/schema.sql", "classpath:/data.sql"})
@Import(FilmDaoImpl.class)
public class FilmDaoTest {

    @Autowired
    private FilmDao filmDao;

    @Test
    public void shouldCreateFilm() {

        var film = filmDao.saveFilm(getFilm());

        assertNotNull(film.getId());
    }

    @Test
    public void shouldUpdateFilm() {

        var film = filmDao.saveFilm(getFilm());

        film.setName("changed value name");
        film.setMpa(new MpaRating(2L, "PG13"));

        var newFilm = filmDao.updateFilm(film);

        assertEquals(film.getId(), newFilm.getId());
        assertEquals(film.getName(), newFilm.getName());
        assertEquals(film.getMpa(), newFilm.getMpa());
    }

    @Test
    public void shouldFindFilmById() {
        Film film = filmDao.saveFilm(getFilm());

        var result = filmDao.findFilmById(film.getId());

        assertTrue(result.isPresent());
        assertEquals(film.getId(), result.get().getId());
        assertEquals(film.getName(), result.get().getName());
        assertEquals(film.getDescription(), result.get().getDescription());
        assertEquals(film.getReleaseDate(), result.get().getReleaseDate());
        assertEquals(film.getDuration(), result.get().getDuration());
        assertEquals(film.getMpa().getId(), result.get().getMpa().getId());
    }

    @Test
    public void shouldFindAllFilms() {
        filmDao.saveFilm(getFilm());
        filmDao.saveFilm(getFilm());
        filmDao.saveFilm(getFilm());

        var result = filmDao.findAllFilms();

        assertNotNull(result);
        assertEquals(3, result.size());
    }
}
