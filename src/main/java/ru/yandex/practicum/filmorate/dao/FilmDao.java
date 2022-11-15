package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {

    Film saveFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> findFilmById(Long filmId);

    List<Film> findAllFilms();

    List<Film> getFilmsByTitle(String title);

    List<Film> findFilmsByDirector(String by);

    List<Film> getFilmsSearchByDirectorAndTitle(String substring);

    List<Film> findFilmsByFriend(Long userId, Long friendId);

    List<Film> findFilmsByGenreAndYear(Long genreId, Integer year);

    List<Film> getFilmsWithRecommendations(Long userId);

    void deleteFilm(Film film);

}
