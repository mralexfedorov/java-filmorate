package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.service.FilmLikeService;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final FilmLikeService filmLikeService;
    private final DirectorService directorService;


    @PostMapping()
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("запрос на создание фильма:" + film);
        return filmService.createFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        log.info("запрос на обновление фильма:" + film);
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") Long filmId) {
        log.info("запрос на получение фильма по идентификатору:" + filmId);
        return filmService.getFilm(filmId);
    }

    @GetMapping()
    public List<Film> findAllFilms() {
        log.info("запрос на получение фильмов");
        return filmService.findAllFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Long filmId,
                        @PathVariable("userId") Long userId) {
        log.info("пользователь:" + userId + "поставил лайк фильму:" + filmId);
        filmLikeService.addLike(filmId, userId);
    }


    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long filmId,
                           @PathVariable("userId") Long userId) {
        log.info("пользователь:" + userId + "удалил лайк фильму:" + filmId);
        filmLikeService.deleteLike(filmId, userId);
    }


    @GetMapping("/popular")
    public List<Film> getMostPopular(@RequestParam(name = "count", required = false) Integer count,
                                     @RequestParam(name = "genreId", required = false) Long genreId,
                                     @RequestParam(name = "year", required = false) Integer year) {
        log.info("запрос на получение популярных фильмов");
        if (genreId != null || year != null) {
            return filmLikeService.getMostPopularByGenreAndYear(count, genreId, year);
        } else {
            return filmLikeService.getMostPopular(count);
        }
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getMostPopularDirectors(@PathVariable("directorId") Long directorId,
                                               @RequestParam("sortBy") String sort) {
        log.info("получение сортированных фильмов режисера:" + directorId);
            return directorService.getDirectorSort(directorId, sort);
    }

    @GetMapping("/search")
    Collection<Film> findFilmByTitleOrDirector(@RequestParam String query,
                                               @RequestParam String by) {
        log.info("началась обработка строки, query=" + query + ",by=" + by);

        List<String> words = new ArrayList<>(Arrays.asList(by.split(",")));
        if (words.size() == 2) {
            return filmService.getFilmsSearchByDirectorAndTitle(query);
        }
        if (words.get(0).equalsIgnoreCase("title")) {
            return filmService.findFilmsByTitle(query);
        }
        if (words.get(0).equalsIgnoreCase("director")) {
            return filmService.findFilmsByDirector(query);
        }
        else return null;
    }
    
    @GetMapping("/common")
    Collection<Film> findFilmsByFriends(@RequestParam Long userId,
                                        @RequestParam Long friendId) {
        log.info("запрос на получение общих с другом фильмов");
        return filmService.findFilmsByFriend(userId, friendId);
    }

    @DeleteMapping("/{filmId}")
    public void  deleteFilm(@PathVariable Long filmId) {
        log.info("запрос на удаление фильма:" + filmId);
        filmService.deleteFilm(filmId);
    }
}
