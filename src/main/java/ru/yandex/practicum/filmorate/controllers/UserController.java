package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendshipService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final FriendshipService friendshipService;

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

    //DELETE /users/{userId}`
    //Удаляет пользователя по идентификатору.
    @DeleteMapping("/users/{userId}")
    public void  deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

}
