package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Events;
import ru.yandex.practicum.filmorate.service.EventsService;
import ru.yandex.practicum.filmorate.storage.EventsStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class EventsServiceImpl implements EventsService {

    private final EventsStorage eventsStorage;



    @Override
    public void addInFriendEvents(Long userId, Long entityId) {
        eventsStorage.addInFriendEvents(userId, entityId);
    }

    @Override
    public void removeFriendEvents(Long userId, Long entityId) {
        eventsStorage.removeFriendEvents(userId, entityId);
    }

    @Override
    public void removePreviewLikeEvents(Long userId, Long entityId) {
        eventsStorage.removePreviewLikeEvents(userId, entityId);
    }

    @Override
    public void addPreviewLikeEvents(Long userId, Long entityId) {
        eventsStorage.addPreviewLikeEvents(userId, entityId);
    }

    @Override
    public void removeReviewEvents(Long userId, Long entityId) {
        eventsStorage.removeReviewEvents(userId, entityId);
    }

    @Override
    public void updatePreviewLikeEvents(Long userId, Long entityId) {
        eventsStorage.updatePreviewLikeEvents(userId, entityId);
    }

    @Override
    public void addReviewEvents(Long userId, Long entityId) {
        eventsStorage.addReviewEvents(userId, entityId);
    }

    @Override
    public void updateReviewEvents(Long userId, Long entityId) {
        eventsStorage.updateReviewEvents(userId, entityId);
    }

    @Override
    public List<Events> getFeedUser(Long userId) {
       return eventsStorage.getFeedUser(userId);
    }
}
