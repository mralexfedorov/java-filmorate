package ru.yandex.practicum.filmorate.controllers;

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
public class UserController {

    private final UserService userService;
    private final FriendshipService friendshipService;
    private final EventsService eventsService;
    private final FilmLikeService filmLikeService;

    @PostMapping("/users")
    public User createUser(@RequestBody @Valid User user) {
        return userService.createUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") Long userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long userId,
                          @PathVariable("friendId") Long friendId) {

        friendshipService.addFriend(userId, friendId);
    }
    
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Long userId,
                             @PathVariable("friendId") Long friendId) {

        friendshipService.deleteFriend(userId, friendId);
    }


    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable("id") Long userId) {

        return friendshipService.getFriends(userId);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Long userId1,
                                       @PathVariable("otherId") Long userId2) {

        return friendshipService.getCommonFriends(userId1, userId2);
    }

    @DeleteMapping("/users/{userId}")
    public void  deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("/users/{id}/feed")
    public List<Events> getFeedUser(@PathVariable("id") Long userId) {
        return eventsService.getFeedUser(userId);
    }

    @GetMapping("/users/{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable("id") Long userId) {

        return filmLikeService.getRecommendations(userId);
    }

}
