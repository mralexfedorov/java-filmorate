package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.MpaRatingDaoImpl;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@JdbcTest
@Sql({"classpath:/schema.sql", "classpath:/data.sql"})
@Import({MpaRatingDaoImpl.class})
public class MpaRatingDaoTest {

    @Autowired
    private MpaRatingDao mpaRatingDao;

    @Test
    public void shouldFindAllMpaRatings() {

        List<MpaRating> result = mpaRatingDao.findAllMpaRatings();

        assertNotNull(result);
        assertEquals(5, result.size());
    }

    @Test
    public void shouldFindMpaRatingById() {

        Optional<MpaRating> mpaRating = mpaRatingDao.findMpaRatingById(4L);

        assertNotNull(mpaRating);
        assertEquals("R", mpaRating.get().getName());
    }

}
