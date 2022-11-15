package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.model.*;

import java.time.LocalDate;
import java.util.List;

public class Fixtures {

    private static Integer filmCount = 1;

    public static Film getFilm() {
        Film film = new Film();
        film.setName("Тестовое имя");
        film.setDescription("Тестовое описание");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.now().minusDays(100));
        film.setMpa(new MpaRating(1L, "PG"));
        film.setId(1L);
        return film;
    }

    public static User getUser() {
        User user = new User();
        user.setName("Тест Тестович");
        user.setEmail("test@email.ru");
        user.setLogin("TestLogin");
        user.setBirthday(LocalDate.of(1987, 11, 22));
        user.setId(1L);
        return user;
    }

    public static Review getReview() {
        Review review = new Review();
        review.setContent("Отзыв положительный");
        review.setIsPositive(true);
        review.setUseful(0);
        return review;
    }



}
