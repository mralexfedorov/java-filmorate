package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.constant.EventsConstant;
import ru.yandex.practicum.filmorate.dao.EventsDao;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.constant.EventsConstant.*;
import static ru.yandex.practicum.filmorate.constant.EventsConstant.USER_ID;


@Component
@AllArgsConstructor
public class EventsDaoImpl implements EventsDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Events> getFeedUser(Long userId) {
        String sql = "select * from events_t where user_id = ?";
        return Collections.singletonList(jdbcTemplate.query(sql, this::eventMapping, userId));
    }

    @Override
    public void saveEvent(Events events) {
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName(EVENTS_TABLE)
                .usingColumns(TIMESTAMP, USER_ID, EVENTTYPE, OPERATION, EVENT_ID)
                .usingGeneratedKeyColumns(ENTITY_ID)
                .executeAndReturnKeyHolder(Map.of(TIMESTAMP, events.getTimestamp(),
                        USER_ID, events.getUserId(),
                        EVENTTYPE, events.getEventType(),
                        OPERATION, events.getOperation(),
                        ENTITY_ID, events.getEventId()))
                .getKeys();
        events.setEventId((Long) keys.get(EVENT_ID));
        jdbcTemplate.update("insert into events_t (user_id, event_type, operation, entity_id, values (?,?,?,?)");

    }


    public Events eventMapping(ResultSet resultSet) throws SQLException {
        Events events = new Events(resultSet.getLong(USER_ID),
                resultSet.getLong(EventsConstant.ENTITY_ID));
        events.setEventId(resultSet.getLong(EventsConstant.EVENT_ID));
        events.setTimestamp(resultSet.getTimestamp(TIMESTAMP).toInstant().toEpochMilli());
        events.setEventType(EventType.valueOf(resultSet.getString(EVENTTYPE)));
        events.setOperation(Operation.valueOf(resultSet.getString(EventsConstant.OPERATION)));
        return events;
    }
}
