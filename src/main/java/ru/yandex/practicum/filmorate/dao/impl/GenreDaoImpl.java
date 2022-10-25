package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constant.GenreConstant;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

import static ru.yandex.practicum.filmorate.constant.GenreConstant.*;


@Component
@AllArgsConstructor
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAllGenres() {
        String sqlToGenreTable = "select * from genre_t";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlToGenreTable);
        List<Genre> allGenres = new ArrayList<>();
        while (genreRows.next()) {
            allGenres.add(mapToGenre(genreRows));
        }
        return allGenres;
    }

    @Override
    public Optional<Genre> findGenreById(Long id) {
        String sqlToGenreTable = "select * from genre_t where id = ? ";

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlToGenreTable, id);
        if (!genreRows.next()) {
            return Optional.empty();
        }
        Genre genre = mapToGenre(genreRows);
        return Optional.of(genre);
    }

    @Override
    public List<Genre> findGenreByFilmId(Long filmId) {

        String sqlToGenreTable = "select gt.id, gt.name from genre_t as gt " +
                "join film_genre_t as fgt  on fgt.genre_id = gt.id " +
                "where fgt.film_id = ? ";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlToGenreTable, filmId);
        List<Genre> allGenres = new ArrayList<>();
        while (genreRows.next()) {
            allGenres.add(mapToGenre(genreRows));
        }
        return allGenres;
    }

    private Genre mapToGenre(SqlRowSet genreRows) {
        return new Genre(
                genreRows.getLong(GenreConstant.ID),
                genreRows.getString(GenreConstant.NAME));
    }
}
