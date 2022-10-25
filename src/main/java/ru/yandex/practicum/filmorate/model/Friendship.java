package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Friendship {

    @Positive(message = "Некорректный номер id.")
    private Long id;

    @Positive(message = "Некорректный номер id.")
    private Long userId;

    @Positive(message = "Некорректный номер id.")
    private Long friendId;

    private Boolean status;
}
