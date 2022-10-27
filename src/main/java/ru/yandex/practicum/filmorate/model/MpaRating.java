package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MpaRating {

    @Positive(message = "Некорректный номер id.")
    private Long id;

    @NotBlank(message = "Введите рейтинг Ассоциации кинокомпаний.")
    private String name;

}
