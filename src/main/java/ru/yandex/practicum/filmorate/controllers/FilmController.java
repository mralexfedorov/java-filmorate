package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmLikeService;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class FilmController {

    private final FilmService filmService;
    private final FilmLikeService filmLikeService;

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
    public List<Film> getMostPopular(@RequestParam(name = "count", required = false) Integer count) {
        return filmLikeService.getMostPopular(count);
    }

//    @GetMapping("/films/director/{directorId}?sortBy=[year,likes]")
//    public List<Film> getMostPopularDirectors (@PathVariable("directorId") Long directorId,
//                                               @RequestParam(name = "count", required = false) Integer count) {
//        return filmLikeService.getMostPopularDirectors(directorId, );
//    }
}
