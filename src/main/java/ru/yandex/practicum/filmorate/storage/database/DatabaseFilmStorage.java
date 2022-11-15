package ru.yandex.practicum.filmorate.storage.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
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
    private final DirectorStorage directorStorage;

    private final FilmGenreDao filmGenreDao;
    private final DirectorDao directorDao;


    @Override
    public Film createFilm(Film film) {
        filmDao.saveFilm(film);
        if (!CollectionUtils.isEmpty(film.getGenres())) {
            for (Genre genre : film.getGenres()) {
                filmGenreDao.linkGenreToFilm(film.getId(), genre.getId());
            }
        }
        if (!CollectionUtils.isEmpty(film.getDirectors())) {
            for (Director director : film.getDirectors()) {
                directorDao.linkDirectorToFilm(film.getId(), director.getId());
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
            film.setDirectors(directorStorage.findDirectorByFilmId(id));
            film.setGenres(genreStorage.findGenreByFilmId(id));
            return film;
        }
        throw new FilmNotFoundException(
                String.format("Фильм с таким id %s не существует", id));
    }

    @Override
    public List<Film> findFilmsByTitle(String title) {
        return filmDao.getFilmsByTitle(title);
    }

    @Override
    public List<Film> findFilmsByDirector(String by) {
        return filmDao.findFilmsByDirector(by);
    }

    @Override
    public List<Film> findFilmsByFriend(Long userId, Long friendId) {
        if (userId < 0 || friendId < 0) {
            throw new UserNotFoundException("Один или оба пользователя не найдены");
        }
        var result= filmDao.findFilmsByFriend(userId, friendId);
        log.info("поиск общих фильмов finish");
        return result;
    }

    @Override
    public List<Film> findFilmsByGenreAndYear(Long genreId, Integer year) {
        return filmDao.findFilmsByGenreAndYear(genreId, year);
    }

    @Override
    public List<Film> getFilmsSearchByDirectorAndTitle(String substring) {
        return filmDao.getFilmsSearchByDirectorAndTitle(substring);
    }

    public void deleteFilm(Long id) {
        Optional<Film> film = filmDao.findFilmById(id);
        filmDao.deleteFilm(film.get());
    }

    @Override
    public Film updateFilm(Film film) {
        findFilm(film.getId());
        filmGenreDao.deleteFilmGenres(film.getId());
        directorDao.deleteFilmDirector(film.getId());
        film = filmDao.updateFilm(film);
        if (!CollectionUtils.isEmpty(film.getGenres())) {
            for (Genre genre : film.getGenres()) {
                filmGenreDao.linkGenreToFilm(film.getId(), genre.getId());
            }
        }
        if (!CollectionUtils.isEmpty(film.getDirectors())) {
            for (Director director : film.getDirectors()) {
                directorDao.linkDirectorToFilm(film.getId(), director.getId());
            }
        }
        film.setDirectors(directorStorage.findDirectorByFilmId(film.getId()));
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
                    film.setDirectors(directorStorage.findDirectorByFilmId(film.getId()));
                    if (film.getMpa() != null & film.getMpa().getId() != null) {
                        film.setMpa(mpaRatingStorage.findMpaRatingById(film.getMpa().getId()));
                    }
                });
        return new ArrayList<>(films);
    }

    @Override
    public List<Film> getFilmsWithRecommendations(Long userId) {
        return filmDao.getFilmsWithRecommendations(userId);
    }

}
