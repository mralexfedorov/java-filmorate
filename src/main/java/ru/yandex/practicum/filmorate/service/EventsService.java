package ru.yandex.practicum.filmorate.service;


import ru.yandex.practicum.filmorate.model.Events;

import java.util.List;
import java.util.Optional;

public interface EventsService {
//    Добавить возможность просмотра последних событий на платформе — добавление в друзья,
//    удаление из друзей, лайки и отзывы,REMOVE, ADD, UPDATE  которые оставили друзья пользователя. Для этого
//    вам понадобится создать таблицу для хранения информации о пользователе — что он
//    лайкнул, кого добавил в друзья, написал ли отзыв
    //public enum EventType {
    //    LIKE, REVIEW, FRIEND


    //добавление в друзья
    void addInFriendEvents(Long userId, Long entityId);

    //удаление из друзей
    void removeFriendEvents(Long userId, Long entityId);

    //просмотр удаления лайка
    void removePreviewLikeEvents(Long userId, Long entityId);

    //просмотр добавления лайка
    void addPreviewLikeEvents(Long userId, Long entityId);


    //удаление просмотра
    void removeReviewEvents(Long userId, Long entityId);

    void updatePreviewLikeEvents(Long userId, Long entityId);

    //добавление просмотра
    void addReviewEvents(Long userId, Long entityId);

    //обновление просмотра
    void updateReviewEvents(Long userId, Long entityId);

    void updateFriendEvents(Long userId, Long entityId);

    List<Events> getFeedUser(Long userId);
}
