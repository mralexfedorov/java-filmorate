package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class MpaRatingController {

    private final MpaRatingService mpaRatingService;

    @GetMapping("/mpa")
    public List<MpaRating> findAllMpaRatings() {
        return mpaRatingService.findAllMpaRatings();
    }

    @GetMapping("/mpa/{id}")
    public MpaRating getMpaRating(@PathVariable("id") Long mpaId) {
        return mpaRatingService.getMpaRating(mpaId);
    }
}
