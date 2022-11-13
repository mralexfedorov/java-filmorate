package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constant.FilmConstant;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constant.FilmConstant.*;
@Slf4j
@Component
@AllArgsConstructor
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;
    GenreDao genreDao;
    GenreStorage genreStorage;

    @Override
    public Film saveFilm(Film film) {
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName(FILM_TABLE)
                .usingColumns(NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_RATING_ID)
                .usingGeneratedKeyColumns(ID)
                .executeAndReturnKeyHolder(Map.of(NAME, film.getName(),
                        DESCRIPTION, film.getDescription(),
                        RELEASE_DATE, java.sql.Date.valueOf(film.getReleaseDate()),
                        DURATION, film.getDuration(),
                        RATE, film.getRate(),
                        MPA_RATING_ID, film.getMpa().getId()))
                .getKeys();
        film.setId((Long) keys.get(ID));
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql =
                "update film_t set id = ?, name = ?, description = ?, release_date = ?, duration = ?," +
                        " rate = ?, mpa_rating_id = ?"
                        + " where id = ? ";
        jdbcTemplate.update(sql,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
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

    @Override
    public List<Film> getFilmsByTitle(String substring) {
        log.info("продолжается обработка строки, query=" + substring);
        String sql = "select * from film_t" +
                " where  LOWER(name) LIKE LOWER('%?%')";
        log.info("обработка sql");

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapToFilm(rs), substring)
                .stream()
                .filter(el -> el != null)
                .collect(Collectors.toList());
    }
    @Override
    public Collection<Film> findFilmsByDirector(String substring) {
        log.info("продолжается обработка строки, query=" + substring);
        String sql = "SELECT * FROM film_t f " +
                " JOIN film_director_t fd  ON f.id = fd.id " +
                " JOIN directors_t dt ON dt.id = fd.director_id " +
                "WHERE LOWER(dt.name) LIKE LOWER('%?%') " +
                "ORDER BY rate DESC";
        log.info("обработка sql");
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapToFilm(rs), substring)
                .stream()
                .filter(el -> el != null)
                .collect(Collectors.toList());
    }
    @Override
    public Collection<Film> getFilmsSearchByDirectorAndTitle(String substring) {
        log.info("продолжается обработка строки, query=" + substring);
        String sql = "SELECT * FROM film_t f" +
                " JOIN film_director_t fd  ON f.id = fd.id " +
                " JOIN directors_t dt ON dt.id = fd.director_id " +
                "WHERE LOWER(dt.name) LIKE LOWER('%?%') " +
                " OR (LOWER(f.name) LIKE LOWER('%?%'))" +
                " ORDER BY rate DESC";
        log.info("обработка sql");
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapToFilm(rs), substring, substring)
                .stream()
                .filter(el -> el != null)
                .collect(Collectors.toList());

    }

    @Override
    public Collection<Film> findFilmsByFriend(Long userId, Long friendId) {
        log.info("поиск общих фильмов");
        String sql1 = "SELECT f.*, mpa.NAME AS mpa_name FROM film_t f" +
                " JOIN MPA_RATING_T mpa on mpa.ID = F.MPA_RATING_ID" +
                " LEFT JOIN FILM_LIKE_T fl on f.ID  = fl.FILM_ID" +
                " WHERE F.ID IN (SELECT FILM_ID FROM FILM_LIKE_T" +
                " WHERE USER_ID = ? OR USER_ID = ?" +
                " GROUP BY FILM_ID" +
                " HAVING COUNT(FILM_ID) > 1)" +
                " GROUP BY FILM_ID" +
                " ORDER BY COUNT(fl.FILM_ID) DESC";

        return jdbcTemplate.query(sql1, (rs, rowNum) -> mapToFilmWithMpaName(rs), userId, friendId)
                .stream()
                .filter(el -> el != null)
                .collect(Collectors.toList());

    }

    @Override
    public Collection<Film> findFilmsByGenreAndYear(Long genreId, Integer year) {
        log.info("поиск фильмов жанр=" + genreId + ", год=" + year);
        String sql = "SELECT * FROM film_t f " +
                     "WHERE ID IN (SELECT FILM_ID FROM film_genre_t " +
                     "WHERE GENRE_ID = ?) " +
                     "AND YEAR(RELEASE_DATE) = ? ";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapToFilm(rs), genreId, year)
                .stream()
                .filter(el -> el != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> getFilmsWithRecommendations(Long userId) {
        String sql = "WITH " +
                     "uf (FILM_ID) AS " +
                     "(SELECT FILM_ID FROM FILM_LIKE_T WHERE USER_ID = ?), " +
                     "nuf (FILM_ID, USER_ID) AS " +
                     "(SELECT FILM_ID, USER_ID FROM FILM_LIKE_T WHERE USER_ID != ?), " +
                     "su (USER_ID) AS " +
                     "(SELECT nuf.USER_ID, " +
                     "COUNT(CASE WHEN nuf.FILM_ID IS NULL THEN 0 " +
                     "ELSE 1 " +
                     "END) AS count " +
                     "FROM uf " +
                     "JOIN nuf " +
                     "ON uf.FILM_ID = nuf.FILM_ID " +
                     "GROUP BY nuf.USER_ID " +
                     "ORDER BY count DESC " +
                     "LIMIT 1) " +
                     "SELECT f.*, mpa.NAME AS mpa_name FROM film_t f " +
                     "JOIN MPA_RATING_T mpa on mpa.ID = F.MPA_RATING_ID " +
                     "WHERE f.ID IN (SELECT nuf.FILM_ID FROM nuf) " +
                     "AND f.ID NOT IN (SELECT uf.FILM_ID FROM uf) ";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapToFilmWithMpaName(rs), userId, userId)
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

    private Film mapToFilmWithMpaName(ResultSet filmRows) throws SQLException {
        var filmId = filmRows.getLong(ID);
        if (filmId <= 0) {
            return null;
        }
        LocalDate releaseDate = filmRows.getDate(FilmConstant.RELEASE_DATE).toLocalDate();
        Film film = new Film(
                filmRows.getLong(FilmConstant.ID),
                filmRows.getString(FilmConstant.NAME),
                filmRows.getString(FilmConstant.DESCRIPTION),
                releaseDate,
                filmRows.getInt(FilmConstant.DURATION),
                MpaRating.builder()
                        .id(filmRows.getLong(FilmConstant.MPA_RATING_ID))
                        .name(filmRows.getString("mpa_name"))
                        .build());

        film.setGenres(genreStorage.findGenreByFilmId(film.getId()));

        return film;
    }

    public void deleteFilm(Film film) {
        String sql = "delete from film_t where id = ? ";
        jdbcTemplate.update(sql, film.getId());
    }
}
