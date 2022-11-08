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
public class FilmController {

    private final FilmService filmService;
    private final FilmLikeService filmLikeService;
    private final DirectorService directorService;


    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable("id") Long filmId) {
        return filmService.getFilm(filmId);
    }

    @GetMapping("/films")
    public List<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Long filmId,
                        @PathVariable("userId") Long userId) {
        filmLikeService.addLike(filmId, userId);
    }


    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long filmId,
                           @PathVariable("userId") Long userId) {
        filmLikeService.deleteLike(filmId, userId);
    }


    @GetMapping("/films/popular")
    public List<Film> getMostPopular(@RequestParam(name = "count", required = false) Integer count,
                                     @RequestParam(name = "genreId", required = false) Long genreId,
                                     @RequestParam(name = "year", required = false) Integer year) {
        if (genreId != null || year != null) {
            return filmLikeService.getMostPopularByGenreAndYear(count, genreId, year);
        } else {
            return filmLikeService.getMostPopular(count);
        }
    }

    @GetMapping("/films/director/{directorId}")
    public List<Film> getMostPopularDirectors(@PathVariable("directorId") Long directorId,
                                               @RequestParam("sortBy") String sort) {
            return directorService.getDirectorSort(directorId, sort);
    }

    @GetMapping("/films/search")
    Collection<Film> findFilmByTitleOrDirector(@RequestParam String query, @RequestParam String by) {
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
        } else return null;
    }
    
    @GetMapping("/films/common")
    Collection<Film> findFilmsByFriends(@RequestParam Long userId,
                                        @RequestParam Long friendId) {
        log.info("запрос на получение общих с другом фильмов");
        return filmService.findFilmsByFriend(userId, friendId);
    }

    @DeleteMapping("/films/{filmId}")
    public void  deleteFilm(@PathVariable Long filmId) {
        filmService.deleteFilm(filmId);
    }
}
