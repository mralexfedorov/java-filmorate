package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping("/directors")
    public List<Director> findAllDirectors() {
        return directorService.findAllDirectors();
    }

    @GetMapping("/directors/{id}")
    public Director getDirector(@PathVariable("id") Long id) {
        return directorService.getDirector(id);
    }

    @PostMapping("/directors")
    public Director createDirector(@Valid @RequestBody Director director) {
        return directorService.createDirector(director);
    }

    @PutMapping("/directors")
    public Director updateDirector(@RequestBody Director director) {
        return directorService.updateDirector(director);
    }

    @DeleteMapping("/directors/{id}")
    public void deleteLike(@PathVariable("id") Long id) {
        directorService.deleteDirector(id);
    }
}
