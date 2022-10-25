package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constant.FriendshipConstant;
import ru.yandex.practicum.filmorate.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.*;

import static ru.yandex.practicum.filmorate.constant.FriendshipConstant.*;
import static ru.yandex.practicum.filmorate.constant.UserConstant.ID;


@Component
@AllArgsConstructor
public class FriendshipDaoImpl implements FriendshipDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Friendship saveFriendship(Friendship friendship) {
        boolean friendshipAlreadyExisted = friendshipStatusRevers(friendship, true);
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName(FRIENDSHIP_TABLE)
                .usingColumns(USER_ID, FRIEND_ID, STATUS)
                .usingGeneratedKeyColumns(ID)
                .executeAndReturnKeyHolder(Map.of(USER_ID, friendship.getUserId(),
                        FRIEND_ID, friendship.getFriendId(),
                        STATUS, friendshipAlreadyExisted ? true : false))
                .getKeys();
        friendship.setId((Long) keys.get(ID));
        return friendship;
    }

    @Override
    public void deleteFriendship(Friendship friendship) {
        friendshipStatusRevers(friendship, false);

        String sqlToFriendshipTable = "delete from friendship_t where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlToFriendshipTable, friendship.getUserId(),
                friendship.getFriendId());
    }

    public Optional<Friendship> findFriendship(Friendship friendship) {
        String sqlToFriendshipTable = "select * from friendship_t where user_id = ? and friend_id = ?";

        SqlRowSet friendshipRows = jdbcTemplate.queryForRowSet(sqlToFriendshipTable, friendship.getUserId(),
                friendship.getFriendId());
        if (!friendshipRows.next()) {
            return Optional.empty();
        }
        friendship = mapToFriendship(friendshipRows);
        return Optional.of(friendship);
    }


    public Set<Long> findFriendIdsByUserId(Long userId) {
        String sqlToFriendshipTable = "select * from friendship_t where user_id = ?";
        SqlRowSet friendshipRows = jdbcTemplate.queryForRowSet(sqlToFriendshipTable, userId);
        Set<Long> friendIds = new HashSet<>();
        while (friendshipRows.next()) {
            friendIds.add(friendshipRows.getLong(FRIEND_ID));
        }
        return friendIds;
    }

    private Friendship updateFriendship(Friendship friendship) {
        String sql = "update friendship_t set id = ?, user_id = ?, friend_id = ?, status = ? " +
                " where id = ? ";
        jdbcTemplate.update(sql,
                friendship.getId(),
                friendship.getUserId(),
                friendship.getFriendId(),
                friendship.getStatus(),
                friendship.getId());
        return friendship;
    }

    private Friendship mapToFriendship(SqlRowSet friendshipRows) {
        return new Friendship(
                friendshipRows.getLong(FriendshipConstant.ID),
                friendshipRows.getLong(USER_ID),
                friendshipRows.getLong(FRIEND_ID),
                friendshipRows.getBoolean(STATUS));
    }

    private boolean friendshipStatusRevers(Friendship friendship, boolean status) {
        Optional<Friendship> existedFriendshipOpt = findFriendship(Friendship.builder()
                .userId(friendship.getFriendId())
                .friendId(friendship.getUserId())
                .build());
        if (existedFriendshipOpt.isPresent()) {
            Friendship existedFriendship = existedFriendshipOpt.get();
            existedFriendship.setStatus(status);
            updateFriendship(existedFriendship);
            return true;
        }
        return false;
    }
}
