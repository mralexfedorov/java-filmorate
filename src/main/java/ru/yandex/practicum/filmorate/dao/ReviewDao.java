package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewDao {

    Review saveReview(Review review);

    Review updateReviewToPositive(Review review);

    Review updateReviewUseful(Review review);

    Optional<Review> findReviewById(Long id);

    void deleteReviewById(Long id);

    List<Review> findAllReviewByFilmId(Long id, Integer count);

    List<Review> findAllReviews();

}
