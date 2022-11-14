package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @GetMapping()
    public List<Genre> findAllGenres() {
        log.info("запрос на получение жанров");
        return genreService.findAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable("id") Long genreId) {
        log.info("запрос на получение жанра:" + genreId);
        return genreService.getGenre(genreId);
    }
    
}
