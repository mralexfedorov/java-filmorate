package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Events;

import java.util.List;

public interface EventsDao {

    List<Events> getFeedUser(Long userId);

    void saveEvent(Events events);


}
