package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmDao {

    Film saveFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> findFilmById(Long filmId);

    List<Film> findAllFilms();

    Collection<Film> getFilmsByTitle(String title);

    Collection<Film> findFilmsByDirector(String by);

    Collection<Film> getFilmsSearchByDirectorAndTitle(String substring);

    Collection<Film> findFilmsByFriend(Long userId, Long friendId);

    Collection<Film> findFilmsByGenreAndYear(Long genreId, Integer year);

    Collection<Film> getFilmsWithUserLikes(Long userId);

    void deleteFilm(Film film);

}
