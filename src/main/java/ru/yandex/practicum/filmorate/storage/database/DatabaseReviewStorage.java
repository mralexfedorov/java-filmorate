package ru.yandex.practicum.filmorate.storage.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.exceptions.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Events;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.EventsService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor
@Primary
public class DatabaseReviewStorage implements ReviewStorage {

    private final ReviewDao reviewDao;
    private final UserService userService;
    private final FilmService filmService;
    private final EventsService eventsService;

    @Override
    public Review createReview(Review review) {
        userService.getUser(review.getUserId());
        filmService.getFilm(review.getFilmId());
        reviewDao.saveReview(review);
        eventsService.addReviewEvents(review.getUserId(), review.getId());
        log.debug("Отзыв {} создан.", review.getId());
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        findReview(review.getId());

        review = reviewDao.updateReviewToPositive(review);

        log.debug("Отзыв {} обновлен.", review.getId());
        return review;
    }

    @Override
    public Review findReview(Long id) {
        Optional<Review> reviewOpt = reviewDao.findReviewById(id);
        if (reviewOpt.isPresent()) {
            var review = reviewOpt.get();
            return review;
        }
        throw new ReviewNotFoundException(
                String.format("Отзыв с таким id %s не существует", id));
    }

    @Override
    public void deleteReview(Long id) {
        findReview(id);
        reviewDao.deleteReviewById(id);
    }

    @Override
    public List<Review> findAllReviewByFilmId(Long id, Integer count) {
        if (id == null || id <= 0) {
            return reviewDao.findAllReviews();
        }

        return reviewDao.findAllReviewByFilmId(id, count);
    }

    @Override
    public Review addLikeReview(Long id, Long userId) {
        userService.getUser(userId);

        var review = findReview(id);

        review.setUseful(review.getUseful() + 1);

        return reviewDao.updateReviewUseful(review);
    }

    @Override
    public Review addDislikeReview(Long id, Long userId) {
        userService.getUser(userId);

        var review = findReview(id);

        review.setUseful(review.getUseful() - 1);

        return reviewDao.updateReviewUseful(review);
    }

}
