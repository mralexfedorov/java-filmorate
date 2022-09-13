package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();
    private Integer filmCount = 1;

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {

        film.setId(getAndIncrement(film));

        films.put(film.getId(), film);
        log.debug("Фильм {} создан.", film.getName());
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {

        if (films.get(film.getId()) == null) {
            throw new FilmNotFoundException(
                    String.format("Пользователь с таким id %s не существует", film.getId()));
        }
        films.put(film.getId(), film);
        log.debug("Данные о фильме {} обновлены.", film.getName());
        return film;
    }

    @GetMapping("/films")
    public List<Film> findAllFilms() {

        return new ArrayList<>(films.values());
    }

    private Integer getAndIncrement(Film film) {
        if (film.getId() != null) {
            return film.getId();
        }
        return filmCount++;
    }
}
