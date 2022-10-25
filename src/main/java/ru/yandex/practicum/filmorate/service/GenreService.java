package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreService {

    Genre getGenre(Long genreId);

    List<Genre> findAllGenres();
}
