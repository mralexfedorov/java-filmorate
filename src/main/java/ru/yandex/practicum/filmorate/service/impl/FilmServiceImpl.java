package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public Film getFilm(Long filmId) {
        return filmStorage.findFilm(filmId);
    }

    @Override
    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        var film = filmStorage.findFilm(filmId);
        var user = userStorage.findUser(userId);
        film.getLikes().add(user.getId());
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        var film = filmStorage.findFilm(filmId);
        var user = userStorage.findUser(userId);
        film.getLikes().remove(user.getId());
    }

    @Override
    public List<Film> getMostPopular(Integer count) {
        if (count == null || count == 0) {
            count = 10;
        }
        Comparator<Film> comparator = Comparator.comparingInt((Film film) -> film.getLikes().size());
        return filmStorage.findAllFilms()
                .stream()
                .sorted(comparator.reversed())
                .limit(count).
                collect(Collectors.toList());
    }
}
