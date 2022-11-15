package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Events;

import java.util.List;

public interface EventsStorage {

    void addInFriendEvents(Long userId, Long entityId);

    void removeFriendEvents(Long userId, Long entityId);

    void removePreviewLikeEvents(Long userId, Long entityId);

    void addPreviewLikeEvents(Long userId, Long entityId);

    void updatePreviewLikeEvents(Long userId, Long entityId);

    void removeReviewEvents(Long userId, Long entityId);

    void addReviewEvents(Long userId, Long entityId);

    void updateReviewEvents(Long userId, Long entityId);

    void updateFriendEvents(Long userId, Long entityId);

    List<Events> getFeedUser(Long userId);
}
