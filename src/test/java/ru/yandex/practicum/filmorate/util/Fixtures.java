package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

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


}
