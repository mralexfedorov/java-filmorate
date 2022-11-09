package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Review {

    @Positive(message = "Некорректный номер id.")
    @JsonProperty("reviewId")
    private Long id;

    @NotBlank(message = "Отзыв не может быть пустым.")
    private String content;

    Boolean isPositive;

    @NotNull
    private Long userId;

    @NotNull
    private Long filmId;

    @Builder.Default
    private Integer useful = 0;

}
