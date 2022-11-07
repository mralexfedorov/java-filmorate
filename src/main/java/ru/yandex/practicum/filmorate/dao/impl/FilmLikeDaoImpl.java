package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constant.FilmLikeConstant.*;


@Component
@AllArgsConstructor
public class FilmLikeDaoImpl implements FilmLikeDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public FilmLike saveLike(FilmLike filmLike) {
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName(FILM_LIKE_TABLE)
                .usingColumns(USER_ID, FILM_ID)
                .usingGeneratedKeyColumns(ID)
                .executeAndReturnKeyHolder(Map.of(USER_ID, filmLike.getUserId(),
                        FILM_ID, filmLike.getFilmId()))
                .getKeys();
        filmLike.setId((Long) keys.get(ID));
        jdbcTemplate.update("UPDATE film_t SET rate = rate + 1");
        return filmLike;
    }

    @Override
    public List<FilmLike> getFilmLikes(Long filmId) {
        String sqlToFilmLikeTable = "select * from film_like_t where film_id = ? ";
        return jdbcTemplate.query(sqlToFilmLikeTable, (rs, rowNum) -> mapToFilmLike(rs), filmId)
                .stream()
                .filter(el -> el != null)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLike(FilmLike filmLike) {
        String sqlToFilmLikeTable = "delete from film_like_t where user_id = ? and film_id = ?";
        jdbcTemplate.update(sqlToFilmLikeTable, filmLike.getUserId(),
                filmLike.getFilmId());
        jdbcTemplate.update("UPDATE film_t SET rate = rate - 1");
    }

    private FilmLike mapToFilmLike(ResultSet filmLikeRows) throws SQLException {
        var userId = filmLikeRows.getLong(USER_ID);
        var filmId = filmLikeRows.getLong(FILM_ID);
        if (userId <= 0 || filmId <= 0) {
            return null;
        }
        return FilmLike.builder()
                .id(filmLikeRows.getLong(ID))
                .userId(filmLikeRows.getLong(USER_ID))
                .filmId(filmLikeRows.getLong(FILM_ID))
                .build();

    }
}
