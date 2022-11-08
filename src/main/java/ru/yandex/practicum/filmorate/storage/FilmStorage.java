package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> findAllFilms();

    Film findFilm(Long id);

    Collection<Film> findFilmsByTitle(String title);

    Collection<Film> getFilmsSearchByDirectorAndTitle(String query);

    Collection<Film> findFilmsByDirector(String by);

    Collection<Film> findFilmsByFriend(Long userId, Long friendId);

    Collection<Film> findFilmsByGenreAndYear(Long genreId, Integer year);

    void deleteFilm(Long id);

}
