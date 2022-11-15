package ru.yandex.practicum.filmorate.controllers;

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
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.util.Fixtures.getUser;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    private static final ObjectMapper om = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController controller;

    @Test
    public void shouldCreateUser() throws Exception {
        User user = getUser();

        String result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(user)))
                .andReturn().getResponse().getContentAsString();

        User savedUser = om.readValue(result, User.class);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(user.getName(), savedUser.getName());
    }

    @Test
    public void shouldNotCreateUserWithIncorrectLogin() throws Exception {
        User user = getUser();

        user.setLogin("Test Login");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(user)))
                .andExpect(status().is5xxServerError())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage()
                                .contains("Логин должен включать буквы, цифры или символы без пробелов.")));

    }

    @Test
    public void shouldNotCreateUserBecauseOfIncorrectEmail() throws Exception {
        User user = getUser();
        user.setEmail("mail.ru");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(user)))
                .andExpect(status().is5xxServerError())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage().contains("Введите email.")));
    }

    @Test
    public void shouldNotCreateUserBecauseOfIncorrectBirthday() throws Exception {
        User user = getUser();
        user.setBirthday(LocalDate.now().plusDays(100));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(user)))
                .andExpect(status().is5xxServerError())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage().contains("Некорректная дата рождения.")));
    }

    @Test
    public void shouldCreateUserWithEmptyName() throws Exception {
        User user = getUser();

        user.setName("");

        String result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(user)))
                .andReturn().getResponse().getContentAsString();

        User savedUser = om.readValue(result, User.class);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(user.getLogin(), savedUser.getName());
    }

    @Test
    public void shouldUpdateLogin() throws Exception {
        User user = getUser();
        controller.createUser(user);
        user.setLogin("НовыйТестЛогин");

        String result = mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(user)))
                .andReturn().getResponse().getContentAsString();

        User savedUser = om.readValue(result, User.class);
        assertEquals(user.getLogin(), savedUser.getLogin());
        assertEquals(user.getId(), savedUser.getId());
    }

    @Test
    public void shouldNotUpdateUserBecauseNotFoundUser() throws Exception {
        User user = getUser();

        controller.createUser(user);
        user.setId(-1L);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(user)))
                .andExpect(status().is4xxClientError())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof UserNotFoundException));
    }
}
