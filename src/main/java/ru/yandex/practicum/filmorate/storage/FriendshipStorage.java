package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Set;

public interface FriendshipStorage {

    Friendship createFriendship(Friendship friendship);

    void deleteFriendship(Friendship friendship);

    Set<Long> findFriendIdsByUserId(Long userId);
}
