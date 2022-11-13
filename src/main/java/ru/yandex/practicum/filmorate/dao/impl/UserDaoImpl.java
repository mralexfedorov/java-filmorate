package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.constant.UserConstant;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constant.UserConstant.*;

@Component
@AllArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User saveUser(User user) {
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName(USER_TABLE)
                .usingColumns(LOGIN, EMAIL, NAME, BIRTHDAY)
                .usingGeneratedKeyColumns(ID)
                .executeAndReturnKeyHolder(Map.of(LOGIN, user.getLogin(),
                        EMAIL, user.getEmail(),
                        NAME, user.getName(),
                        BIRTHDAY, java.sql.Date.valueOf(user.getBirthday())))
                .getKeys();
        user.setId((Long) keys.get(ID));
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "update user_t set id = ?, login = ?, email = ?, name = ?, birthday = ? " +
                " where id = ? ";
        jdbcTemplate.update(sql,
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        String sqlToUserTable = "select * from user_t";
        return jdbcTemplate.query(sqlToUserTable, (rs, rowNum) -> mapToUser(rs))
                .stream()
                .filter(el -> el != null)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findUserById(Long id) {

        String sqlToUserTable = "select * from user_t where id = ? ";
        return jdbcTemplate.query(sqlToUserTable, (rs, rowNum) -> mapToUser(rs), id)
                .stream()
                .filter(el -> el != null)
                .findFirst();
    }

    @Override
    public List<User> findAllUserByIds(Set<Long> ids) {
        List<User> users = new ArrayList<>();
        if (CollectionUtils.isEmpty(ids)) {
            return users;
        }
        String inSql = ids.stream().map(Object::toString)
                .collect(Collectors.joining(", "));
        String sqlToUserTable = String.format("select * from user_t where id in ( %s )", inSql);
        return jdbcTemplate.query(sqlToUserTable, (rs, rowNum) -> mapToUser(rs))
                .stream()
                .filter(el -> el != null)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(User user) {
        String sql = "delete from user_t where id = ? ";
        jdbcTemplate.update(sql, user.getId());
    }


    private User mapToUser(ResultSet userRows) throws SQLException { //
        var userId = userRows.getLong(ID);
        if (userId <= 0) {
            return null;
        }
        LocalDate birthday = userRows.getDate(UserConstant.BIRTHDAY).toLocalDate();
        return new User(
                userRows.getLong(UserConstant.ID),
                userRows.getString(UserConstant.LOGIN),
                userRows.getString(UserConstant.EMAIL),
                userRows.getString(UserConstant.NAME),
                birthday);
    }

}
