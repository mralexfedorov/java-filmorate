package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.service.FilmLikeService;
import ru.yandex.practicum.filmorate.storage.FilmLikeStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FilmLikeServiceImpl implements FilmLikeService {

    private final FilmLikeStorage filmLikeStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public void addLike(Long filmId, Long userId) {
        filmLikeStorage.saveLike(FilmLike.builder()
                .filmId(filmId)
                .userId(userId)
                .build());
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        userStorage.findUserById(userId);
        filmLikeStorage.deleteLike(FilmLike.builder()
                .filmId(filmId)
                .userId(userId)
                .build());
    }


    @Override
    public List<Film> getMostPopular(Integer count) {
        if (count == null || count == 0) {
            count = 10;
        }
        Comparator<Film> comparator = Comparator.comparingInt((Film film) -> film.getLikes().size());
        return filmStorage.findAllFilms()
                .stream()
                .peek(el -> {
                    List<FilmLike> likes = filmLikeStorage.getFilmLikes(el.getId());
                    if (!CollectionUtils.isEmpty(likes)) {
                        el.setLikes(likes.stream()
                                .map(FilmLike::getFilmId)
                                .collect(Collectors.toList()));
                    }
                })
                .sorted(comparator.reversed())
                .limit(count).
                collect(Collectors.toList());
    }

    @Override
    public List<Film> getMostPopularByGenreAndYear(Integer count, Long genreId, Integer year) {
        Comparator<Film> comparator = Comparator.comparingInt((Film film) -> film.getLikes().size());
        return filmStorage.findFilmsByGenreAndYear(genreId, year)
                .stream()
                .peek(el -> {
                    List<FilmLike> likes = filmLikeStorage.getFilmLikes(el.getId());
                    if (!CollectionUtils.isEmpty(likes)) {
                        el.setLikes(likes.stream()
                                .map(FilmLike::getFilmId)
                                .collect(Collectors.toList()));
                    }
                })
                .sorted(comparator.reversed())
                .limit(count).
                collect(Collectors.toList());
    }

    @Override
    public List<Film> getRecommendations(Long userId) {
        return filmStorage.getFilmsWithRecommendations(userId);
    }
}
