package ru.yandex.practicum.filmorate.storage.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.exceptions.MpaRatingNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
@Primary
public class DatabaseMpaRatingStorage implements MpaRatingStorage {

    private final MpaRatingDao mpaRatingDao;

    @Override
    public List<MpaRating> findAllMpaRatings() {
        return mpaRatingDao.findAllMpaRatings();
    }

    @Override
    public MpaRating findMpaRatingById(Long id) {
        var mpaRating = mpaRatingDao.findMpaRatingById(id);
        if (mpaRating.isPresent()) {
            return mpaRating.get();
        }
        throw new MpaRatingNotFoundException(
                String.format("Рейтинг с таким id %s не существует", id));
    }

}
