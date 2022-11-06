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
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public Review createReview(@Valid @RequestBody Review review) {
        return reviewService.createReview(review);
    }

    @PutMapping("/reviews")
    public Review updateReview(@RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    @GetMapping("/reviews/{id}")
    public Review getReviewById(@PathVariable("id") Long id) {
        return reviewService.getReview(id);
    }

    @DeleteMapping("/reviews/{id}")
    public void deleteReview(@PathVariable("id") Long id) {
        reviewService.deleteReview(id);
    }

    @GetMapping("/reviews")
    public List<Review> getAllReviewByFilmId(@RequestParam(name = "filmId", required = false) Long filmId,
                                             @RequestParam(name = "count", defaultValue = "10",
                                                     required = false) Integer count) {
        return reviewService.getAllReviewByFilmId(filmId, count);
    }

    @PutMapping("/reviews/{id}/like/{userId}")
    public Review addLikeReview(@PathVariable("id") Long id,
                                @PathVariable("userId") Long userId) {
        return reviewService.addLikeReview(id, userId);
    }

    @PutMapping("/reviews/{id}/dislike/{userId}")
    public Review addDislikeReview(@PathVariable("id") Long id,
                                   @PathVariable("userId") Long userId) {
        return reviewService.addDislikeReview(id, userId);
    }

    @DeleteMapping("/reviews/{id}/like/{userId}")
    public Review deleteLikeReview(@PathVariable("id") Long id,
                                   @PathVariable("userId") Long userId) {
        return reviewService.addDislikeReview(id, userId);
    }

    @DeleteMapping("/reviews/{id}/dislike/{userId}")
    public Review deleteDislikeReview(@PathVariable("id") Long id,
                                   @PathVariable("userId") Long userId) {
        return reviewService.addLikeReview(id, userId);
    }
}
