package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Optional;
import java.util.Set;

public interface FriendshipDao {
    Friendship saveFriendship(Friendship friendship);

    void deleteFriendship(Friendship friendship);

    public Set<Long> findFriendIdsByUserId(Long userId);

    public Optional<Friendship> findFriendship(Friendship friendship);
}
