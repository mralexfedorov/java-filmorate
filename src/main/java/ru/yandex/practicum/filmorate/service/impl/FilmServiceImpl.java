package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;

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
    public Collection<Film> findFilmsByTitle(String title) {
        return filmStorage.findFilmsByTitle(title);
    }
    @Override
    public Collection<Film> getFilmsSearchByDirectorAndTitle(String query) {
        return filmStorage.getFilmsSearchByDirectorAndTitle(query);
    }

    @Override
    public Collection<Film> findFilmsByDirector(String by) {
        return filmStorage.findFilmsByDirector(by);
    }

    @Override
    public Collection<Film> findFilmsByFriend(Long userId, Long friendId) {
        return filmStorage.findFilmsByFriend(userId, friendId);
    }

    @Override
    public void deleteFilm(Long filmId) {
        filmStorage.deleteFilm(filmId);
    }

}
