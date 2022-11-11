package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Events {

    private Long timestamp;
    @Positive(message = "Некорректный номер id.")
    private Long userId;
    private EventType eventType; // одно из значениий LIKE, REVIEW или FRIEND
    private Operation operation; // одно из значениий REMOVE, ADD, UPDATE
    @Positive(message = "Некорректный номер id.")
    private Long eventId; //primary key
    @Positive(message = "Некорректный номер id.")
    private Long entityId; // идентификатор сущности, с которой произошло событие

    public Events(Long userId, Long entityId) {
        this.userId = userId;
        this.entityId = entityId;
    }
}
