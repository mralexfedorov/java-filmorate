package ru.yandex.practicum.filmorate.storage.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.storage.FilmLikeStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
@Primary
public class DatabaseFilmLikeStorage implements FilmLikeStorage {

    private final FilmLikeDao filmLikeDao;
    private final FilmStorage filmStorage;

    @Override
    public FilmLike saveLike(FilmLike filmLike) {
        filmLikeDao.saveLike(filmLike);
        log.debug("Ваш лайк учтен.");
        return filmLike;
    }

    @Override
    public List<FilmLike> getFilmLikes(Long filmId) {
        return filmLikeDao.getFilmLikes(filmId);
    }

    @Override
    public void deleteLike(FilmLike filmLike) {
        filmStorage.findFilm(filmLike.getFilmId());
        filmLikeDao.deleteLike(filmLike);
        log.debug("Ваш лайк удален.");
    }

}
