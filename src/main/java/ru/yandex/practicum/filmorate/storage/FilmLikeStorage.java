package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmLike;

import java.util.List;

public interface FilmLikeStorage {

    FilmLike saveLike(FilmLike filmLike);

    List<FilmLike> getFilmLikes(Long filmId);

    void deleteLike(FilmLike filmLike);
}
