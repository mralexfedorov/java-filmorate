package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.util.Fixtures.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerTest {
    private static final ObjectMapper om = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewController controller;

    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private FilmDao filmDao;

    @Autowired
    private UserDao userDao;

    private Film film;

    private User user;

    @Test
    public void shouldCreateReview() throws Exception {

        user = userDao.saveUser(getUser());
        film = filmDao.saveFilm(getFilm());

        var review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(film.getId());


        String resultReview = mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(review)))
                .andReturn().getResponse().getContentAsString();

        Review savedReview = om.readValue(resultReview, Review.class);
        assertNotNull(savedReview);
        assertNotNull(savedReview.getId());
        assertEquals(review.getContent(), savedReview.getContent());
    }

    @Test
    public void shouldNotCreateReviewBecauseOfEmptyContent() throws Exception {
        user = userDao.saveUser(getUser());
        film = filmDao.saveFilm(getFilm());

        var review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(film.getId());
        review.setContent(" ");

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(review)))
                .andExpect(status().is5xxServerError())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage().contains("Отзыв не может быть пустым.")));
    }


    @Test
    public void shouldNotCreateFilmBecauseUserIdNegative() throws Exception {

        film = filmDao.saveFilm(getFilm());

        var review = getReview();

        review.setFilmId(film.getId());
        review.setUserId(-1L);

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(review)))
                .andExpect(status().is4xxClientError())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof UserNotFoundException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage().contains(
                                "Пользователь с таким id -1 не существует")));
    }

    @Test
    public void shouldNotCreateFilmBecauseFilmIdNegative() throws Exception {

        user = userDao.saveUser(getUser());
        var review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(-1L);

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(review)))
                .andExpect(status().is4xxClientError())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof FilmNotFoundException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage().contains(
                                "Фильм с таким id -1 не существует")));
    }

    @Test
    public void shouldUpdateReview() throws Exception {
        user = userDao.saveUser(getUser());
        film = filmDao.saveFilm(getFilm());

        var review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(film.getId());

        var reviewResult = reviewDao.saveReview(review);

        reviewResult.setIsPositive(false);
        reviewResult.setContent("Отзыв отрицательный.");

        String result = mockMvc.perform(put("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(reviewResult)))
                .andReturn().getResponse().getContentAsString();

        Review savedReview = om.readValue(result, Review.class);
        assertEquals(reviewResult.getIsPositive(), savedReview.getIsPositive());
        assertEquals(reviewResult.getContent(), savedReview.getContent());
        assertEquals(reviewResult.getId(), savedReview.getId());
    }

    @Test
    public void shouldGetReviewById() throws Exception {
        user = userDao.saveUser(getUser());
        film = filmDao.saveFilm(getFilm());

        var review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(film.getId());

        review = reviewDao.saveReview(review);

        String result = mockMvc.perform(get("/reviews/" + review.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        Review savedReview = om.readValue(result, Review.class);
        assertEquals(review.getId(), savedReview.getId());
        assertEquals(review.getUserId(), savedReview.getUserId());
        assertEquals(review.getFilmId(), savedReview.getFilmId());
        assertEquals(review.getIsPositive(), savedReview.getIsPositive());
        assertEquals(review.getContent(), savedReview.getContent());
    }

    @Test
    public void shouldDeleteReviewById() throws Exception {
        user = userDao.saveUser(getUser());
        film = filmDao.saveFilm(getFilm());

        var review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(film.getId());

        review = reviewDao.saveReview(review);

        int status = mockMvc.perform(delete("/reviews/" + review.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getStatus();

        assertEquals(200, status);
    }

    @Test
    public void shouldGetReviewByFilmId() throws Exception {
        user = userDao.saveUser(getUser());
        film = filmDao.saveFilm(getFilm());

        var review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(film.getId());

        reviewDao.saveReview(review);

        var review2 = getReview();

        review2.setUserId(user.getId());
        review2.setFilmId(film.getId());

        reviewDao.saveReview(review2);

        String result = mockMvc.perform(get("/reviews?filmId=" + film.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        List<Review> savedReview = om.readValue(result, new TypeReference<List<Review>>(){});
        assertEquals(2, savedReview.size());
    }

    @Test
    public void shouldAddLikeReview() throws Exception{
        user = userDao.saveUser(getUser());
        film = filmDao.saveFilm(getFilm());

        var review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(film.getId());

        reviewDao.saveReview(review);

        String result = mockMvc.perform(put("/reviews/" + review.getId() + "/like/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(review)))
                .andReturn().getResponse().getContentAsString();

        Review resultReview = om.readValue(result, Review.class);
        assertEquals(1, resultReview.getUseful());
    }

    @Test
    public void shouldAddDislikeReview() throws Exception{
        user = userDao.saveUser(getUser());
        film = filmDao.saveFilm(getFilm());

        var review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(film.getId());

        reviewDao.saveReview(review);

        String result = mockMvc.perform(put("/reviews/" + review.getId() + "/dislike/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(review)))
                .andReturn().getResponse().getContentAsString();

        Review resultReview = om.readValue(result, Review.class);
        assertEquals(-1, resultReview.getUseful());
    }

    @Test
    public void shouldDeleteLikeReview() throws Exception{
        user = userDao.saveUser(getUser());
        film = filmDao.saveFilm(getFilm());

        var review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(film.getId());

        reviewDao.saveReview(review);

        String result = mockMvc.perform(delete("/reviews/" + review.getId() + "/like/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(review)))
                .andReturn().getResponse().getContentAsString();

        Review resultReview = om.readValue(result, Review.class);
        assertEquals(-1, resultReview.getUseful());
    }

    @Test
    public void shouldDeleteDislikeReview() throws Exception{
        user = userDao.saveUser(getUser());
        film = filmDao.saveFilm(getFilm());

        var review = getReview();

        review.setUserId(user.getId());
        review.setFilmId(film.getId());

        reviewDao.saveReview(review);

        String result = mockMvc.perform(delete("/reviews/" + review.getId() + "/dislike/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(review)))
                .andReturn().getResponse().getContentAsString();

        Review resultReview = om.readValue(result, Review.class);
        assertEquals(1, resultReview.getUseful());
    }

}
