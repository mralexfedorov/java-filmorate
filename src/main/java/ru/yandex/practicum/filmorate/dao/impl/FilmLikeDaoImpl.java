package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        return filmLike;
    }

    @Override
    public List<FilmLike> getFilmLikes(Long filmId) {
        String sqlToFilmLikeTable = "select * from film_like_t where film_id = ? ";

        SqlRowSet likeRows = jdbcTemplate.queryForRowSet(sqlToFilmLikeTable, filmId);
        List<FilmLike> filmLikes = new ArrayList<>();
        while (likeRows.next()) {
            filmLikes.add(mapToFilmLike(likeRows));
        }
        return filmLikes;
    }

    @Override
    public void deleteLike(FilmLike filmLike) {
        String sqlToFilmLikeTable = "delete from film_like_t where user_id = ? and film_id = ?";
        jdbcTemplate.update(sqlToFilmLikeTable, filmLike.getUserId(),
                filmLike.getFilmId());
    }

    private FilmLike mapToFilmLike(SqlRowSet filmLikeRows) {
        return FilmLike.builder()
                .id(filmLikeRows.getLong(ID))
                .userId(filmLikeRows.getLong(USER_ID))
                .filmId(filmLikeRows.getLong(FILM_ID))
                .build();
    }
}
