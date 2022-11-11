package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Events;

import java.util.List;

public interface EventsStorage {

    //добавление в друзья
    void addInFriendEvents(Long userId, Long entityId);

    //удаление из друзей
    void removeFriendEvents(Long userId, Long entityId);

    //просмотр удаления лайка
    void removePreviewLikeEvents(Long userId, Long entityId);

    //просмотр добавления лайка
    void addPreviewLikeEvents(Long userId, Long entityId);

    void updatePreviewLikeEvents(Long userId, Long entityId);

    //удаление просмотра
    void removeReviewEvents(Long userId, Long entityId);

    //добавление просмотра
    void addReviewEvents(Long userId, Long entityId);

    //обновление просмотра
    void updateReviewEvents(Long userId, Long entityId);

    List<Events> getFeedUser(Long userId);
}
