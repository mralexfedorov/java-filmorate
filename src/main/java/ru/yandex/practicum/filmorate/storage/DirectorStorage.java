package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    List<Director> findAllDirectors();

    Director findDirectorById(Long id);

    Director createDirector(Director director);

    Director updateDirector(Director director);

    void deleteDirector(Long id);

    List<Director> findDirectorByFilmId(Long id);
}
