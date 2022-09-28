package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.BeforeNow;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    @Positive(message = "Некорректный номер id.")
    private Long id;

    @Email(message = "Введите email.")
    @NotBlank(message = "Введите email.")
    private String email;

    @NotBlank
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9\\*]*$",
            message = "Логин должен включать буквы, цифры или символы без пробелов.")
    private String login;

    private String name;

    @BeforeNow(message = "Некорректная дата рождения.")
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();
}
