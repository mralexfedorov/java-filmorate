package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public User getUser(Long userId) {
        return userStorage.findUser(userId);
    }

    @Override
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        var user = userStorage.findUser(userId);
        var friend = userStorage.findUser(friendId);
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        var user = userStorage.findUser(userId);
        var friend = userStorage.findUser(friendId);
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
    }

    @Override
    public List<User> getFriends(Long userId) {
        var user = userStorage.findUser(userId);
        var friendIds = user.getFriends();
        return userStorage.findAllUsersByIds(friendIds);
    }


    @Override
    public List<User> getCommonFriends(Long userId1, Long userId2) {
        var user1 = userStorage.findUser(userId1);
        var user2 = userStorage.findUser(userId2);
        var friend1Ids = user1.getFriends();
        var friend2Ids = user2.getFriends();
        var commonFriendIds = findCommonElements(friend1Ids, friend2Ids);
        return userStorage.findAllUsersByIds(commonFriendIds);
    }


    private static <T> Set<T> findCommonElements(Set<T> first, Set<T> second) {
        return first.stream().filter(second::contains).collect(Collectors.toSet());
    }
}
