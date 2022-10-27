package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.BeforeNow;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
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

    @JsonIgnore
    private Set<Long> friends = new HashSet<>();

    public User(Long id, String login, String email, String name, LocalDate birthday) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.name = name;
        this.birthday = birthday;
    }

}
