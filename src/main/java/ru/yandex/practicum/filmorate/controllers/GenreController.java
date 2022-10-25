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
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genres")
    public List<Genre> findAllGenres() {
        return genreService.findAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable("id") Long genreId) {
        return genreService.getGenre(genreId);
    }
    
}
