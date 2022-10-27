package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaRatingServiceImpl implements MpaRatingService {

    private final MpaRatingStorage mpaRatingStorage;

    @Override
    public MpaRating getMpaRating(Long mpaRatingId) {
        return mpaRatingStorage.findMpaRatingById(mpaRatingId);
    }

    @Override
    public List<MpaRating> findAllMpaRatings() {
        return mpaRatingStorage.findAllMpaRatings();
    }
}
