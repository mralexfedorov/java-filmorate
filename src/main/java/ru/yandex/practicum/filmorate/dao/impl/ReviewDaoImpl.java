package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constant.ReviewConstant.*;

@Component
@AllArgsConstructor
public class ReviewDaoImpl implements ReviewDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Review saveReview(Review review) {
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName(REVIEW_TABLE)
                .usingColumns(CONTENT, IS_POSITIVE, USER_ID, FILM_ID, USEFUL)
                .usingGeneratedKeyColumns(ID)
                .executeAndReturnKeyHolder(Map.of(CONTENT, review.getContent(),
                        IS_POSITIVE, review.getIsPositive(),
                        USER_ID, review.getUserId(),
                        FILM_ID, review.getFilmId(),
                        USEFUL, review.getUseful()))
                .getKeys();
        review.setId((Long) keys.get(ID));
        return review;
    }

    @Override
    public Review updateReviewToPositive(Review review) {
        String sql =
                "update review_t set content = ?, is_positive = ? "
                        + " where id = ? ";
        jdbcTemplate.update(sql,
                review.getContent(),
                review.getIsPositive(),
                review.getId());
        return review;
    }


    @Override
    public Review updateReviewUseful(Review review) {
        String sql =
                "update review_t set useful = ? "
                        + " where id = ? ";
        jdbcTemplate.update(sql,
                review.getUseful(),
                review.getId());
        return review;
    }
    @Override
    public Optional<Review> findReviewById(Long id) {

        String sqlToReviewTable = "select * from review_t where id = ? ";
        return jdbcTemplate.query(sqlToReviewTable, (rs, rowNum) -> mapToReview(rs), id)
                .stream()
                .filter(el -> el != null)
                .findFirst();
    }

    @Override
    public void deleteReviewById(Long id) {

        String sqlToReviewTable = "delete from review_t where id = ? ";
        jdbcTemplate.update(sqlToReviewTable, id);
    }

    @Override
    public List<Review> findAllReviewByFilmId(Long filmId, Integer count) {
        String sqlToReviewTable = "select * from review_t where film_id = ? order by useful DESC   limit ?";
        return jdbcTemplate.query(sqlToReviewTable, (rs, rowNum) -> mapToReview(rs), filmId, count)
                .stream()
                .filter(el -> el != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> findAllReviews() {
        String sqlToReviewTable = "select * from review_t order by useful DESC";
        return jdbcTemplate.query(sqlToReviewTable, (rs, rowNum) -> mapToReview(rs))
                .stream()
                .filter(el -> el != null)
                .collect(Collectors.toList());
    }

    private Review mapToReview(ResultSet reviewRows) throws SQLException {
        Long reviewId = reviewRows.getLong(ID);
        if (reviewId <= 0) {
            return null;
        }
        return new Review(
                reviewRows.getLong(ID),
                reviewRows.getString(CONTENT),
                reviewRows.getBoolean(IS_POSITIVE),
                reviewRows.getLong(USER_ID),
                reviewRows.getLong(FILM_ID),
                reviewRows.getInt(USEFUL));
    }

}
