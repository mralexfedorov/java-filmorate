package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constant.FilmConstant;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return jdbcTemplate.query(sqlToFilmTable, (rs, rowNum) -> mapToFilm(rs), id)
                .stream()
                .filter(el -> el != null)
                .findFirst();
    }

    @Override
    public List<Film> findAllFilms() {
        String sqlToFilmTable = "select * from film_t";
        return jdbcTemplate.query(sqlToFilmTable, (rs, rowNum) -> mapToFilm(rs))
                .stream()
                .filter(el -> el != null)
                .collect(Collectors.toList());
    }


    private Film mapToFilm(ResultSet filmRows) throws SQLException {
        var filmId = filmRows.getLong(ID);
        if (filmId <= 0) {
            return null;
        }
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
