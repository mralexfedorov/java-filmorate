package ru.yandex.practicum.filmorate.storage.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.EventsDao;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Events;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.EventsStorage;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor
@Primary
public class DatabaseEventsStorage implements EventsStorage {

    private final EventsDao eventsDao;


    @Override
    public void addInFriendEvents(Long userId, Long entityId) {
        Events eventsFriend = new Events(userId, entityId);
        eventsFriend.setEventType(EventType.FRIEND);
        eventsFriend.setOperation(Operation.ADD);
        eventsDao.saveEvent(eventsFriend);

    }

    @Override
    public void addReviewEvents(Long userId, Long entityId) {
        Events eventsReview = new Events(userId, entityId);
        eventsReview.setEventType(EventType.REVIEW);
        eventsReview.setOperation(Operation.ADD);
        eventsDao.saveEvent(eventsReview);
    }

    @Override
    public void removeFriendEvents(Long userId, Long entityId) {
        Events eventsFriend = new Events(userId, entityId);
        eventsFriend.setEventType(EventType.FRIEND);
        eventsFriend.setOperation(Operation.REMOVE);
        eventsDao.saveEvent(eventsFriend);
    }

    @Override
    public void removePreviewLikeEvents(Long userId, Long entityId) {
        Events eventsLike = new Events(userId, entityId);
        eventsLike.setEventType(EventType.LIKE);
        eventsLike.setOperation(Operation.REMOVE);
        eventsDao.saveEvent(eventsLike);
    }

    @Override
    public void addPreviewLikeEvents(Long userId, Long entityId) {
        Events eventsLike = new Events(userId, entityId);
        eventsLike.setEventType(EventType.LIKE);
        eventsLike.setOperation(Operation.ADD);
        eventsDao.saveEvent(eventsLike);
    }

    @Override
    public void updatePreviewLikeEvents(Long userId, Long entityId) {
        Events eventsLike = new Events(userId, entityId);
        eventsLike.setEventType(EventType.LIKE);
        eventsLike.setOperation(Operation.UPDATE);
        eventsDao.saveEvent(eventsLike);
    }

    @Override
    public void removeReviewEvents(Long userId, Long entityId) {
        Events eventsReview = new Events(userId, entityId);
        eventsReview.setEventType(EventType.REVIEW);
        eventsReview.setOperation(Operation.REMOVE);
        eventsDao.saveEvent(eventsReview);
    }

    @Override
    public void updateReviewEvents(Long userId, Long entityId) {
        Events eventsReview = new Events(userId, entityId);
        eventsReview.setEventType(EventType.REVIEW);
        eventsReview.setOperation(Operation.UPDATE);
        eventsDao.saveEvent(eventsReview);
    }

    @Override
    public List<Events> getFeedUser(Long userId) {
        return eventsDao.getFeedUser(userId);
    }
}
