package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;
import java.util.Optional;


import java.util.List;

public interface DirectorDao {
    List<Director> findAllDirectors();

    void linkDirectorToFilm(Long filmId, Long directorId);

    void saveDirector(Director director);

    Optional<Director> findDirectorById(Long id);

    Director updateDirector(Director director);

    List<Director> findDirectorByFilmId(Long filmId);

    void deleteFilmDirector(Long id);

    void deleteDirector(Long id);
}
