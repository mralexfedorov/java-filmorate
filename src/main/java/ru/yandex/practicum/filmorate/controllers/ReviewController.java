package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping()
    public Review createReview(@Valid @RequestBody Review review) {
        log.info("получен запрос на создание отзыва:" + review);
        return reviewService.createReview(review);
    }

    @PutMapping()
    public Review updateReview(@RequestBody Review review) {
        log.info("получен запрос на обновление отзыва:" + review);
        return reviewService.updateReview(review);
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable("id") Long id) {
        log.info("получен запрос на получение отзыва:" + id);
        return reviewService.getReview(id);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable("id") Long id) {
        log.info("получен запрос на удаление отзыва:" + id);
        reviewService.deleteReview(id);
    }

    @GetMapping()
    public List<Review> getAllReviewByFilmId(@RequestParam(name = "filmId", required = false) Long filmId,
                                             @RequestParam(name = "count", defaultValue = "10",
                                                     required = false) Integer count) {
        log.info("получен запрос на получение отзывов на фильм:" + filmId);
        return reviewService.getAllReviewByFilmId(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public Review addLikeReview(@PathVariable("id") Long id,
                                @PathVariable("userId") Long userId) {
        log.info("запрос на добавление лайка пользователем:" + userId + "отзыву:" + id);
        return reviewService.addLikeReview(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public Review addDislikeReview(@PathVariable("id") Long id,
                                   @PathVariable("userId") Long userId) {
        log.info("запрос на добавление дизлайка пользователем:" + userId + "отзыву:" + id);
        return reviewService.addDislikeReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Review deleteLikeReview(@PathVariable("id") Long id,
                                   @PathVariable("userId") Long userId) {
        log.info("запрос на удаление лайка пользователем:" + userId + "отзыву:" + id);
        return reviewService.addDislikeReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public Review deleteDislikeReview(@PathVariable("id") Long id,
                                   @PathVariable("userId") Long userId) {
        log.info("запрос на удаление дизлайка пользователем:" + userId + "отзыву:" + id);
        return reviewService.addLikeReview(id, userId);
    }
}
