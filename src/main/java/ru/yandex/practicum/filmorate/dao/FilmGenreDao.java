package ru.yandex.practicum.filmorate.dao;

public interface FilmGenreDao {

    void linkGenreToFilm(Long filmId, Long genreId);

    void deleteFilmGenres(Long filmId);

    boolean linkAlreadyExist(Long filmId, Long genreId);
}
