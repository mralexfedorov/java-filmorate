package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constant.DirectorConstant;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constant.DirectorConstant.*;
import static ru.yandex.practicum.filmorate.constant.FilmConstant.ID;
import static ru.yandex.practicum.filmorate.constant.FilmLikeConstant.FILM_ID;

@Component
@AllArgsConstructor
public class DirectorDaoImpl implements DirectorDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Director> findAllDirectors() {
        String sqlToDirectorTable = "select * from directors_t";
        return jdbcTemplate.query(sqlToDirectorTable, (rs, rowNum) -> mapToDirector(rs))
                .stream()
                .filter(el -> el != null)
                .collect(Collectors.toList());
    }

    @Override
    public void linkDirectorToFilm(Long filmId, Long directorId) {
        if (linkAlreadyExist(filmId, directorId)) {
            return;
        }
        new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName(FILM_DIRECTOR_TABLE)
                .usingColumns(DirectorConstant.FILM_ID, DIRECTOR_ID)
                .usingGeneratedKeyColumns(DIRECTORS_ID)
                .executeAndReturnKeyHolder(Map.of(FILM_ID, filmId,
                        DIRECTOR_ID, directorId))
                .getKeys();
    }

    @Override
    public void saveDirector(Director director) {
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName(DIRECTORS_TABLE)
                .usingColumns(DIRECTORS_NAME)
                .usingGeneratedKeyColumns(DIRECTORS_ID)
                .executeAndReturnKeyHolder(Map.of(DIRECTORS_NAME, director.getName())).getKeys();
        director.setId((Long) keys.get(DIRECTORS_ID));
    }

    @Override
    public Optional<Director> findDirectorById(Long id) {
        String sqlToDirectorTable = "select * from directors_t where id = ? ";
        return jdbcTemplate.query(sqlToDirectorTable, (rs, rowNum) -> mapToDirector(rs), id)
                .stream()
                .filter(el -> el != null)
                .findFirst();
    }

    @Override
    public Director updateDirector(Director director) {
        String sql =
                "update directors_t set name = ? where id = ? ";
        jdbcTemplate.update(sql,
                director.getName(),
                director.getId());
        return director;
    }

    @Override
    public List<Director> findDirectorByFilmId(Long filmId) {
        String sqlToDirectorTable = "select d.id, d.name from directors_t as d " +
                "join film_director_t as fd  on fd.director_id = d.id " +
                "where fd.film_id = ? ";
        return jdbcTemplate.query(sqlToDirectorTable, (rs, rowNum) -> mapToDirector(rs), filmId)
                .stream()
                .filter(el -> el != null)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFilmDirector(Long id) {
        String sqlToFilmDirectorTable = "delete from film_director_t where film_id = ?";
        jdbcTemplate.update(sqlToFilmDirectorTable, id);
    }

    @Override
    public void deleteDirector(Long id) {
        String sqlToDeleteFilmDirectorTable = "delete from film_director_t where director_id = ?";
        jdbcTemplate.update(sqlToDeleteFilmDirectorTable, id);
        String sqlToDeleteDirector = "delete from directors_t where id = ?";
        jdbcTemplate.update(sqlToDeleteDirector, id);
    }


    public boolean linkAlreadyExist(Long filmId, Long directorId) {

        String sqlToDirectorTable = "select * from film_director_t where film_id = ? and director_id = ? ";

        SqlRowSet directorRows = jdbcTemplate.queryForRowSet(sqlToDirectorTable, filmId, directorId);
        if (directorRows.next()) {
            return true;
        }
        return false;
    }

    private Director mapToDirector(ResultSet directorRows) throws SQLException {
        Long filmId = directorRows.getLong(ID);
        if (filmId <= 0) {
            return null;
        }
        return new Director(
                directorRows.getLong(DIRECTORS_ID),
                directorRows.getString(DIRECTORS_NAME));
    }
}
