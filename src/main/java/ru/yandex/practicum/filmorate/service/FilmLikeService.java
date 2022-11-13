package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmLikeService {

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getMostPopular(Integer count);

    List<Film> getMostPopularByGenreAndYear(Integer count, Long genreId, Integer year);

    List<Film> getRecommendations(Long userId);
}
