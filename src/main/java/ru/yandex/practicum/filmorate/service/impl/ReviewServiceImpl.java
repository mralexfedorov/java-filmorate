package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewStorage reviewStorage;

    @Override
    public Review createReview(Review review) {
        return reviewStorage.createReview(review);
    }

    @Override
    public Review getReview(Long id) {
        return reviewStorage.findReview(id);
    }

    @Override
    public Review updateReview(Review review) {
        return reviewStorage.updateReview(review);
    }

    @Override
    public void deleteReview(Long id) {
        reviewStorage.deleteReview(id);
    }

    @Override
    public List<Review> getAllReviewByFilmId(Long id, Integer count){
       return reviewStorage.findAllReviewByFilmId(id, count);
    }

    @Override
    public Review addLikeReview(Long id, Long userId) {
        return reviewStorage.addLikeReview(id, userId);
    }

    @Override
    public Review addDislikeReview(Long id, Long userId) {
        return reviewStorage.addDislikeReview(id, userId);
    }


}
