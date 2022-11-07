package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilm(Long filmId);

    List<Film> findAllFilms();

    void deleteFilm(Long filmId);

}
