package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewService {

    Review createReview(Review review);

    Review updateReview(Review review);

    Review getReview(Long id);

    void deleteReview(Long id);

    List<Review> getAllReviewByFilmId(Long id, Integer count);

    Review addLikeReview(Long id, Long userId);

    Review addDislikeReview(Long id, Long userId);
}
