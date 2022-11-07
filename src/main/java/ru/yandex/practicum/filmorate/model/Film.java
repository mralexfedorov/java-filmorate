package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.validation.After;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    private List<Long> likes = new ArrayList<>();

    private List<Genre> genres;

    private List<Director> directors;

    private MpaRating mpa;

    public Film(Long id, String name, String description,
                LocalDate releaseDate, Integer duration, MpaRating mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }
}
