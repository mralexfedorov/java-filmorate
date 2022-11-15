package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> findAllFilms();

    Film findFilm(Long id);

    List<Film> findFilmsByTitle(String title);

    List<Film> getFilmsSearchByDirectorAndTitle(String query);

    List<Film> findFilmsByDirector(String by);

    List<Film> findFilmsByFriend(Long userId, Long friendId);

    List<Film> findFilmsByGenreAndYear(Long genreId, Integer year);

    List<Film> getFilmsWithRecommendations(Long userId);

    void deleteFilm(Long id);

}
