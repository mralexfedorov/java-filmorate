package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmService {

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilm(Long filmId);

    List<Film> findAllFilms();

    Collection<Film> getFilmsSearchByDirectorAndTitle(String query);

    Collection<Film> findFilmsByTitle(String query);

    Collection<Film> findFilmsByDirector(String query);

    Collection<Film> findFilmsByFriend(Long userId, Long friendId);
}
