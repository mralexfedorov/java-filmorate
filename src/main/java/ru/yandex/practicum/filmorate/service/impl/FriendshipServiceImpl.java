package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendshipService;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendshipStorage friendshipStorage;
    private final UserStorage userStorage;

    @Override
    public Friendship addFriend(Long userId, Long friendId) {
        userStorage.findUserById(userId);
        userStorage.findUserById(friendId);
        return friendshipStorage.createFriendship(Friendship.builder()
                .userId(userId)
                .friendId(friendId)
                .build());
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        friendshipStorage.deleteFriendship(Friendship.builder()
                .userId(userId)
                .friendId(friendId)
                .build());
    }

    @Override
    public List<User> getFriends(Long userId) {
        Set<Long> friendIds = friendshipStorage.findFriendIdsByUserId(userId);
        return userStorage.findAllUsersByIds(friendIds);
    }

    @Override
    public List<User> getCommonFriends(Long userId1, Long userId2) {
        Set<Long> friend1Ids = friendshipStorage.findFriendIdsByUserId(userId1);
        Set<Long> friend2Ids = friendshipStorage.findFriendIdsByUserId(userId2);
        var commonFriendIds = findCommonElements(friend1Ids, friend2Ids);
        return userStorage.findAllUsersByIds(commonFriendIds);
    }

    private static <T> Set<T> findCommonElements(Set<T> first, Set<T> second) {
        return first.stream().filter(second::contains).collect(Collectors.toSet());
    }
}
