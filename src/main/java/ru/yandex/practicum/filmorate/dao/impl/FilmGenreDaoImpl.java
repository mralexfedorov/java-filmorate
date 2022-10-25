package ru.yandex.practicum.filmorate.dao.impl;


import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.constant.FilmGenreConstant.*;


@Component
@AllArgsConstructor
public class FilmGenreDaoImpl implements FilmGenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void linkGenreToFilm(Long filmId, Long genreId) {
        if (linkAlreadyExist(filmId, genreId)) {
            return;
        }
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName(FILM_GENRE_TABLE)
                .usingColumns(FILM_ID, GENRE_ID)
                .usingGeneratedKeyColumns(ID)
                .executeAndReturnKeyHolder(Map.of(FILM_ID, filmId,
                        GENRE_ID, genreId))
                .getKeys();
    }

    @Override
    public void deleteFilmGenres(Long filmId) {
        String sqlToGenreTable = "delete from film_genre_t where film_id = ?";
        jdbcTemplate.update(sqlToGenreTable, filmId);
    }

    public boolean linkAlreadyExist(Long filmId, Long genreId) {

        String sqlToGenreTable = "select * from film_genre_t where film_id = ? and genre_id = ? ";

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlToGenreTable, filmId, genreId);
        if (genreRows.next()) {
            return true;
        }
        return false;
    }
}
