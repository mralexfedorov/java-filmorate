package ru.yandex.practicum.filmorate.storage.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.util.Set;

@Component
@Slf4j
@AllArgsConstructor
@Primary
public class DatabaseFriendshipStorage implements FriendshipStorage {

    private final FriendshipDao friendshipDao;

    @Override
    public Friendship createFriendship(Friendship friendship) {
        return friendshipDao.saveFriendship(friendship);
    }

    @Override
    public void deleteFriendship(Friendship friendship) {
        friendshipDao.deleteFriendship(friendship);
    }

    @Override
    public Set<Long> findFriendIdsByUserId(Long userId) {
        return friendshipDao.findFriendIdsByUserId(userId);
    }
}
