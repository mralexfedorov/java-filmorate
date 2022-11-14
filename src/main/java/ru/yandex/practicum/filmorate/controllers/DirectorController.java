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
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping()
    public List<Director> findAllDirectors() {
        log.info("получение всех режиссеров");
        return directorService.findAllDirectors();
    }

    @GetMapping("/{id}")
    public Director getDirector(@PathVariable("id") Long id) {
        log.info("запрос на получение режиссера:" + id);
        return directorService.getDirector(id);
    }

    @PostMapping()
    public Director createDirector(@Valid @RequestBody Director director) {
        log.info("запрос на создание режиссера:" + director);
        return directorService.createDirector(director);
    }

    @PutMapping()
    public Director updateDirector(@RequestBody Director director) {
        log.info("запрос на изменение режиссера:" + director);
        return directorService.updateDirector(director);
    }

    @DeleteMapping("/{id}")
    public void deleteDirector(@PathVariable("id") Long id) {
        log.info("запрос на удаление режиссера:" + id);
        directorService.deleteDirector(id);
    }
}
