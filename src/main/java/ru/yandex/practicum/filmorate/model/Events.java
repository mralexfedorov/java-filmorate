package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Events {

    private Long timestamp;

    private Long userId;
    private EventType eventType;
    private Operation operation;
    private Long eventId;
    private Long entityId;


    public Events(Long userId, Long entityId) {
        this.userId = userId;
        this.entityId = entityId;
    }
}
