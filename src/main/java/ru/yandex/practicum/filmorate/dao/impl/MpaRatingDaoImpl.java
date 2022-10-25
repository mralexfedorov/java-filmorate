package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constant.MpaRatingConstant;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class MpaRatingDaoImpl implements MpaRatingDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<MpaRating> findAllMpaRatings() {
        String sqlToMpaRatingTable = "select * from mpa_rating_t";
        SqlRowSet mpaRatingRows = jdbcTemplate.queryForRowSet(sqlToMpaRatingTable);
        List<MpaRating> allMpaRating = new ArrayList<>();
        while (mpaRatingRows.next()) {
            allMpaRating.add(mapToMpaRating(mpaRatingRows));
        }
        return allMpaRating;
    }

    @Override
    public Optional<MpaRating> findMpaRatingById(Long id) {
        String sqlToMpaRatingTable = "select * from mpa_rating_t where id = ? ";

        SqlRowSet mpaRatingRows = jdbcTemplate.queryForRowSet(sqlToMpaRatingTable, id);
        if (!mpaRatingRows.next()) {
            return Optional.empty();
        }
        MpaRating mpaRating = mapToMpaRating(mpaRatingRows);
        return Optional.of(mpaRating);
    }

    private MpaRating mapToMpaRating(SqlRowSet mpaRatingRows) {
        return new MpaRating(
                mpaRatingRows.getLong(MpaRatingConstant.ID),
                mpaRatingRows.getString(MpaRatingConstant.NAME));
    }
}
