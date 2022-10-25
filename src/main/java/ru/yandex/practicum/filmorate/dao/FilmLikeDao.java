package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.FilmLike;

import java.util.List;

public interface FilmLikeDao {

    FilmLike saveLike(FilmLike filmLike);

    List<FilmLike> getFilmLikes(Long filmId);

    void deleteLike(FilmLike filmLike);

}
