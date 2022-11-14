package ru.yandex.practicum.filmorate.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Events;
import ru.yandex.practicum.filmorate.service.EventsService;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmLikeService;
import ru.yandex.practicum.filmorate.service.FriendshipService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final FriendshipService friendshipService;
    private final EventsService eventsService;
    private final FilmLikeService filmLikeService;

    @PostMapping()
    public User createUser(@RequestBody @Valid User user) {
        log.info("получен запрос на создание пользователя:" + user);
        return userService.createUser(user);
    }

    @PutMapping()
    public User updateUser(@RequestBody User user) {
        log.info("получен запрос на обновление пользователя:" + user);
        return userService.updateUser(user);
    }

    @GetMapping()
    public List<User> findAllUsers() {
        log.info("получен запрос на получение всех пользователей");
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Long userId) {
        log.info("запрос на получение пользователя:" + userId);
        return userService.getUser(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long userId,
                          @PathVariable("friendId") Long friendId) {
        log.info("запрос на добавление друга:" + friendId + "пользователю:" + userId);
        friendshipService.addFriend(userId, friendId);
    }
    
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Long userId,
                             @PathVariable("friendId") Long friendId) {
        log.info("запрос на удаление друга:" + friendId + " у пользователя:" + userId);
        friendshipService.deleteFriend(userId, friendId);
    }


    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") Long userId) {
        log.info("запрос на получение друзей пользователя:" + userId);
        return friendshipService.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Long userId1,
                                       @PathVariable("otherId") Long userId2) {
        log.info("запрос на получение общих друзей пользователя:" + userId1 + "и пользователя:" + userId2);
        return friendshipService.getCommonFriends(userId1, userId2);
    }

    @DeleteMapping("/{userId}")
    public void  deleteUser(@PathVariable Long userId) {
        log.info("запрос на удаление пользователя:" + userId);
        userService.deleteUser(userId);
    }

    @GetMapping("/{id}/feed")
    public List<Events> getFeedUser(@PathVariable("id") Long userId) {
        log.info("получение ленты событий пользователя:" + userId);
        return eventsService.getFeedUser(userId);
    }

    @GetMapping("/{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable("id") Long userId) {
        log.info("запрос на получение рекомендаций по фильмам:" + userId);
        return filmLikeService.getRecommendations(userId);
    }

}
