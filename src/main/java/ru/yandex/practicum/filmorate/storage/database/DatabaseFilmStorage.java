package ru.yandex.practicum.filmorate.storage.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor
@Primary
public class DatabaseFilmStorage implements FilmStorage {

    private final FilmDao filmDao;
    private final MpaRatingStorage mpaRatingStorage;
    private final GenreStorage genreStorage;
    private final FilmGenreDao filmGenreDao;

    @Override
    public Film createFilm(Film film) {
        filmDao.saveFilm(film);
        if (!CollectionUtils.isEmpty(film.getGenres())) {
            for (Genre genre : film.getGenres()) {
                filmGenreDao.linkGenreToFilm(film.getId(), genre.getId());
            }
        }
        log.debug("Фильм {} создан.", film.getName());
        return film;
    }

    @Override
    public Film findFilm(Long id) {
        Optional<Film> filmOpt = filmDao.findFilmById(id);
        if (filmOpt.isPresent()) {
            var film = filmOpt.get();
            if (film.getMpa() != null & film.getMpa().getId() != null) {
                film.setMpa(mpaRatingStorage.findMpaRatingById(film.getMpa().getId()));
            }
            film.setGenres(genreStorage.findGenreByFilmId(id));
            return film;
        }
        throw new FilmNotFoundException(
                String.format("Фильм с таким id %s не существует", id));
    }

    @Override
    public Film updateFilm(Film film) {
        findFilm(film.getId());
        filmGenreDao.deleteFilmGenres(film.getId());
        film = filmDao.updateFilm(film);
        if (!CollectionUtils.isEmpty(film.getGenres())) {
            for (Genre genre : film.getGenres()) {
                filmGenreDao.linkGenreToFilm(film.getId(), genre.getId());
            }
        }
        film.setGenres(genreStorage.findGenreByFilmId(film.getId()));
        log.debug("Данные о фильме {} обновлены.", film.getName());
        return film;
    }

    @Override
    public List<Film> findAllFilms() {
        var films = filmDao.findAllFilms();
        if (CollectionUtils.isEmpty(films)) {
            return films;
        }
        films.stream()
                .forEach(film -> {
                    film.setGenres(genreStorage.findGenreByFilmId(film.getId()));
                    if (film.getMpa() != null & film.getMpa().getId() != null) {
                        film.setMpa(mpaRatingStorage.findMpaRatingById(film.getMpa().getId()));
                    }
                });
        return new ArrayList<>(films);
    }

}
