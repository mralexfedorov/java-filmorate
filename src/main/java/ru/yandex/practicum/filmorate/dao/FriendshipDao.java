package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Optional;
import java.util.Set;

public interface FriendshipDao {
    Friendship saveFriendship(Friendship friendship);

    Long deleteFriendship(Friendship friendship);

    Set<Long> findFriendIdsByUserId(Long userId);

    Optional<Friendship> findFriendship(Friendship friendship);
}
