package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.validation.After;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {

    @Positive(message = "Некорректный номер id.")
    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;

    @Length(max = 200, message = "Длина описания не должна превышать 200 символов.")
    private String description;

    @After("1895-12-28")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма не может быть отрицательной.")
    private Integer duration;

    private Set<Long> likes = new HashSet<>();
}
