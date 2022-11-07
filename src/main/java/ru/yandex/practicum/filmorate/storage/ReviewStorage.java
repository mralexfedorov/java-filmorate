package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {

    Review createReview(Review review);

    Review updateReview(Review review);

    Review findReview(Long id);

    void deleteReview(Long id);

    List<Review> findAllReviewByFilmId(Long id, Integer count);

    Review addLikeReview(Long id, Long userId);

    Review addDislikeReview(Long id, Long userId);

}
