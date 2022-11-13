package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Events {

    private Long timestamp;

    private Long userId;
    private EventType eventType; // одно из значениий LIKE, REVIEW или FRIEND
    private Operation operation; // одно из значениий REMOVE, ADD, UPDATE
    private Long eventId; //primary key
    private Long entityId; // идентификатор сущности, с которой произошло событие


    public Events(Long userId, Long entityId) {
        this.userId = userId;
        this.entityId = entityId;
    }
}
