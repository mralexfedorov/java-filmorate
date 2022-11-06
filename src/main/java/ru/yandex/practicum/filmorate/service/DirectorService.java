package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface DirectorService {
    List<Director> findAllDirectors();

    Director getDirector(Long id);

    Director createDirector(Director director);

    Director updateDirector(Director director);

    void deleteDirector(Long id);

    List<Film> getDirectorSort(Long directorId, String str);
}
