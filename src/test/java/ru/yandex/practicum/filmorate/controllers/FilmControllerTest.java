package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.util.Fixtures.getFilm;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@WebMvcTest(FilmController.class)
public class FilmControllerTest {
    private static final ObjectMapper om = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmController controller;

    @Test
    public void shouldCreateFilm() throws Exception {
        var film = getFilm();

        String result = mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(film)))
                .andReturn().getResponse().getContentAsString();

        Film savedFilm = om.readValue(result, Film.class);
        assertNotNull(savedFilm);
        assertNotNull(savedFilm.getId());
        assertEquals(film.getName(), savedFilm.getName());
    }

    @Test
    public void shouldNotCreateFilmBecauseOfEmptyName() throws Exception {
        var film = getFilm();
        film.setName(" ");

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(film)))
                .andExpect(status().is4xxClientError())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage().contains("Название фильма не может быть пустым.")));
    }

    @Test
    public void shouldNotCreateFilmBecauseOfIncorrectReleaseDate() throws Exception {
        var film = getFilm();
        film.setReleaseDate(LocalDate.of(1888, 12, 28));

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(film)))
                .andExpect(status().is4xxClientError())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage().contains("должен быть после 1895-12-28")));
    }

    @Test
    public void shouldNotCreateFilmBecauseDescriptionMore200() throws Exception {
        var film = getFilm();
        film.setDescription("Тестовое описание Тестовое описание Тестовое описание Тестовое описание " +
                "Тестовое описание Тестовое описание Тестовое описание Тестовое описание " +
                "Тестовое описание Тестовое описание Тестовое описание Тестовое описание " +
                "Тестовое описание Тестовое описание Тестовое описание Тестовое описание.");

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(film)))
                .andExpect(status().is4xxClientError())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage().contains(
                                "Длина описания не должна превышать 200 символов.")));
    }

    @Test
    public void shouldNotCreateFilmBecauseDurationNegative() throws Exception {
        var film = getFilm();
        film.setDuration(-120);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(film)))
                .andExpect(status().is4xxClientError())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage().contains(
                                "Продолжительность фильма не может быть отрицательной.")));
    }

    @Test
    public void shouldUpdateFilm() throws Exception {
        var film = getFilm();
        controller.createFilm(film);

        film.setDuration(120);
        film.setName("Новое тестовое имя.");


        String result = mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(film)))
                .andReturn().getResponse().getContentAsString();

        Film savedFilm = om.readValue(result, Film.class);
        assertEquals(film.getDuration(), savedFilm.getDuration());
        assertEquals(film.getName(), savedFilm.getName());
        assertEquals(film.getId(), savedFilm.getId());
    }

    @Test
    public void shouldNotUpdateFilmBecauseNotFoundFilm() throws Exception {
        var film = getFilm();

        controller.createFilm(film);
        film.setId(-1);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(film)))
                .andExpect(status().is4xxClientError())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof FilmNotFoundException));
    }
}
