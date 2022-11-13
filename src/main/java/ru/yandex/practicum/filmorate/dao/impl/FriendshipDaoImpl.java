package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constant.FriendshipConstant;
import ru.yandex.practicum.filmorate.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Long deleteFriendship(Friendship friendship) {
        friendshipStatusRevers(friendship, false);

        String sqlToFriendshipTableId = "select FRIEND_ID  from FRIENDSHIP_T where user_id = ? and friend_id = ?";

        var resultSet = jdbcTemplate.queryForRowSet(sqlToFriendshipTableId,
                        friendship.getUserId(),
                        friendship.getFriendId());
        if(!resultSet.next()){
          return null;
        }
        var idFriend = resultSet.getLong("FRIEND_ID");

        String sqlToFriendshipTable = "delete from friendship_t where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlToFriendshipTable, friendship.getUserId(),
                friendship.getFriendId());

        return idFriend;
    }

    public Optional<Friendship> findFriendship(Friendship friendship) {
        String sqlToFriendshipTable = "select * from friendship_t where user_id = ? and friend_id = ?";

        return jdbcTemplate.query(sqlToFriendshipTable, (rs, rowNum) -> mapToFriendship(rs),
                        friendship.getUserId(),
                        friendship.getFriendId())
                .stream()
                .filter(el -> el != null)
                .findFirst();
    }


    public Set<Long> findFriendIdsByUserId(Long userId) {
        String sqlToFriendshipTable = "select * from friendship_t where user_id = ?";
        return jdbcTemplate.query(sqlToFriendshipTable, (rs, rowNum) -> mapToFriendship(rs), userId)
                .stream()
                .filter(el -> el != null)
                .map(el -> el.getFriendId())
                .collect(Collectors.toSet());
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

    private Friendship mapToFriendship(ResultSet friendshipRows) throws SQLException {
        var userId = friendshipRows.getLong(USER_ID);
        var friendId = friendshipRows.getLong(FRIEND_ID);
        if (userId <= 0 || friendId <= 0) {
            return null;
        }
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
