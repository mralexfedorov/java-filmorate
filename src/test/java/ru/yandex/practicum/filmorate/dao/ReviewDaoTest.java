package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.ReviewDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.util.Fixtures.*;

@JdbcTest
@Sql({"classpath:/schema.sql", "classpath:/data.sql"})
@Import({ReviewDaoImpl.class, UserDaoImpl.class, FilmDaoImpl.class})
public class ReviewDaoTest {

    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private FilmDao filmDao;

    @Autowired
    private UserDao userDao;

    private Film film;

    private User user;

    @BeforeEach
    public void setUp() {
        user = userDao.saveUser(getUser());
        film = filmDao.saveFilm(getFilm());

        Review review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(film.getId());

        reviewDao.saveReview(review);

    }


    @Test
    public void shouldCreateReview() {

        Review review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(film.getId());

        reviewDao.saveReview(review);

        assertNotNull(review.getId());
    }

    @Test
    public void shouldUpdateReviewIsNotPositive() {

        Review review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(film.getId());

        reviewDao.saveReview(review);

        review.setIsPositive(false);
        review.setContent("Отзыв отрицательный");

        Review newReview = reviewDao.updateReviewToPositive(review);

        assertEquals(review.getId(), newReview.getId());
        assertEquals(review.getContent(), newReview.getContent());
        assertEquals(review.getIsPositive(), newReview.getIsPositive());
    }

    @Test
    public void shouldUpdateReviewUseful() {

        Review review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(film.getId());

        reviewDao.saveReview(review);

        review.setUseful(10);

        Review newReview = reviewDao.updateReviewToPositive(review);

        assertEquals(review.getId(), newReview.getId());
        assertEquals(review.getUseful(), newReview.getUseful());
    }


    @Test
    public void shouldFindReviewById() {

        User user = userDao.saveUser(getUser());
        Film film = filmDao.saveFilm(getFilm());

        Review review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(film.getId());

        Review newReview = reviewDao.saveReview(review);

        Optional<Review> result = reviewDao.findReviewById(newReview.getId());

        assertTrue(result.isPresent());
        assertEquals(review.getId(), result.get().getId());
        assertEquals(review.getContent(), result.get().getContent());
        assertEquals(review.getIsPositive(), result.get().getIsPositive());
        assertEquals(review.getUserId(), result.get().getUserId());
        assertEquals(review.getFilmId(), result.get().getFilmId());
        assertEquals(review.getUseful(), result.get().getUseful());
    }

    @Test
    public void shouldDeleteReviewById() {

        User user = userDao.saveUser(getUser());
        Film film = filmDao.saveFilm(getFilm());

        Review review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(film.getId());

        reviewDao.saveReview(review);

        reviewDao.deleteReviewById(review.getId());

        Optional<Review> result = reviewDao.findReviewById(review.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldFindAllReviewByFilmId() {

        User user = userDao.saveUser(getUser());
        Film film = filmDao.saveFilm(getFilm());

        Review review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(film.getId());

        reviewDao.saveReview(review);

        Review review2 = getReview();

        review2.setUserId(user.getId());
        review2.setFilmId(film.getId());

        reviewDao.saveReview(review2);

        Review review3 = getReview();

        review3.setUserId(user.getId());
        review3.setFilmId(film.getId());

        reviewDao.saveReview(review3);

        List<Review> result = reviewDao.findAllReviewByFilmId(film.getId(), 2);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void  shouldFindAllReviews() {

        User user = userDao.saveUser(getUser());
        Film film = filmDao.saveFilm(getFilm());

        Review review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(film.getId());

        reviewDao.saveReview(review);

        Review review2 = getReview();

        review2.setUserId(user.getId());
        review2.setFilmId(film.getId());

        reviewDao.saveReview(review2);

        Review review3 = getReview();

        review3.setUserId(user.getId());
        review3.setFilmId(film.getId());

        reviewDao.saveReview(review3);

        List<Review> result = reviewDao.findAllReviews();

        assertNotNull(result);
        assertEquals(4, result.size());
    }
}
