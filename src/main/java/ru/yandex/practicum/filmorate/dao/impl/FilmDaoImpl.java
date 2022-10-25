package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constant.FilmConstant;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;
import java.util.*;

import static ru.yandex.practicum.filmorate.constant.FilmConstant.*;

@Component
@AllArgsConstructor
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film saveFilm(Film film) {
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName(FILM_TABLE)
                .usingColumns(NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING_ID)
                .usingGeneratedKeyColumns(ID)
                .executeAndReturnKeyHolder(Map.of(NAME, film.getName(),
                        DESCRIPTION, film.getDescription(),
                        RELEASE_DATE, java.sql.Date.valueOf(film.getReleaseDate()),
                        DURATION, film.getDuration(),
                        MPA_RATING_ID, film.getMpa().getId()))
                .getKeys();
        film.setId((Long) keys.get(ID));
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql =
                "update film_t set id = ?, name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ?"
                        + " where id = ? ";
        jdbcTemplate.update(sql,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public Optional<Film> findFilmById(Long id) {

        String sqlToFilmTable = "select * from film_t where id = ? ";

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlToFilmTable, id);
        if (!filmRows.next()) {
            return Optional.empty();
        }
        Film film = mapToFilm(filmRows);
        return Optional.of(film);
    }

    @Override
    public List<Film> findAllFilms() {
        String sqlToFilmTable = "select * from film_t";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlToFilmTable);
        List<Film> allFilms = new ArrayList<>();
        while (filmRows.next()) {
            allFilms.add(mapToFilm(filmRows));
        }
        return allFilms;
    }


    private Film mapToFilm(SqlRowSet filmRows) { //
        LocalDate releaseDate = filmRows.getDate(FilmConstant.RELEASE_DATE).toLocalDate();
        return new Film(
                filmRows.getLong(FilmConstant.ID),
                filmRows.getString(FilmConstant.NAME),
                filmRows.getString(FilmConstant.DESCRIPTION),
                releaseDate,
                filmRows.getInt(FilmConstant.DURATION),
                MpaRating.builder()
                        .id(filmRows.getLong(FilmConstant.MPA_RATING_ID))
                        .build());
    }
}
