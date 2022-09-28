package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;


public interface FilmService {

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilm(Long filmId);

    List<Film> findAllFilms();

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getMostPopular(Integer count);
}
