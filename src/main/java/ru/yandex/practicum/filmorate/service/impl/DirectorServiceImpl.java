package ru.yandex.practicum.filmorate.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {

    private final DirectorStorage directorStorage;
    private final FilmStorage filmStorage;


    @Override
    public List<Director> findAllDirectors() {
        return directorStorage.findAllDirectors();
    }

    @Override
    public Director getDirector(Long id) {
        return directorStorage.findDirectorById(id);
    }

    @Override
    public Director createDirector(Director director) {
        return directorStorage.createDirector(director);
    }

    @Override
    public Director updateDirector(Director director) {
        return directorStorage.updateDirector(director);
    }

    @Override
    public void deleteDirector(Long id) {
        directorStorage.findDirectorById(id);
        directorStorage.deleteDirector(id);
    }

    @Override
    public List<Film> getDirectorSort(Long directorId, String str) {
        Director director = directorStorage.findDirectorById(directorId);
        if(str.contains("year")) {
            return filmStorage.findAllFilms().stream()
                    .filter(c -> c.getDirectors().contains(director))
                    .sorted(Comparator.comparing(Film::getReleaseDate))
                    .collect(Collectors.toList());
        } else if (str.contains("likes")) {
            return filmStorage.findAllFilms().stream()
                    .filter(c -> c.getDirectors().contains(director))
                    .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()))
                    .collect(Collectors.toList());
        } else {
            throw  new DirectorNotFoundException("Сортировки " + str +" не существует");
        }
    }
}
