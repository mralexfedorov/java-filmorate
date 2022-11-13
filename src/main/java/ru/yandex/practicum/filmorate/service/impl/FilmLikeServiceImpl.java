package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmLikeService;
import ru.yandex.practicum.filmorate.storage.FilmLikeStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FilmLikeServiceImpl implements FilmLikeService {

    private final FilmLikeStorage filmLikeStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public void addLike(Long filmId, Long userId) {
        filmLikeStorage.saveLike(FilmLike.builder()
                .filmId(filmId)
                .userId(userId)
                .build());
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        userStorage.findUserById(userId);
        filmLikeStorage.deleteLike(FilmLike.builder()
                .filmId(filmId)
                .userId(userId)
                .build());
    }


    @Override
    public List<Film> getMostPopular(Integer count) {
        if (count == null || count == 0) {
            count = 10;
        }
        Comparator<Film> comparator = Comparator.comparingInt((Film film) -> film.getLikes().size());
        return filmStorage.findAllFilms()
                .stream()
                .peek(el -> {
                    List<FilmLike> likes = filmLikeStorage.getFilmLikes(el.getId());
                    if (!CollectionUtils.isEmpty(likes)) {
                        el.setLikes(likes.stream()
                                .map(FilmLike::getFilmId)
                                .collect(Collectors.toList()));
                    }
                })
                .sorted(comparator.reversed())
                .limit(count).
                collect(Collectors.toList());
    }

    @Override
    public List<Film> getMostPopularByGenreAndYear(Integer count, Long genreId, Integer year) {
        Comparator<Film> comparator = Comparator.comparingInt((Film film) -> film.getLikes().size());
        return filmStorage.findFilmsByGenreAndYear(genreId, year)
                .stream()
                .peek(el -> {
                    List<FilmLike> likes = filmLikeStorage.getFilmLikes(el.getId());
                    if (!CollectionUtils.isEmpty(likes)) {
                        el.setLikes(likes.stream()
                                .map(FilmLike::getFilmId)
                                .collect(Collectors.toList()));
                    }
                })
                .sorted(comparator.reversed())
                .limit(count).
                collect(Collectors.toList());
    }

    @Override
    public List<Film> getRecommendations(Long userId) {
        Map<Long, HashMap<Film, Double>> inputData = new HashMap<>();
        Map<Long, HashMap<Film, Double>> outputData = new HashMap<>();
        Map<Film, HashMap<Film, Double>> diff = new HashMap<>();
        Map<Film, HashMap<Film, Integer>> freq = new HashMap<>();

        Collection<Film> films = new ArrayList<>();

        // подготовка исходных данных
        for (User user: userStorage.findAllUsers()) {
            Collection<Film> filmsWithUserLikes = filmStorage.getFilmsWithUserLikes(user.getId());
            
            HashMap<Film, Double> userFilms = new HashMap<>();
            for (Film film: filmsWithUserLikes) {
                if (!films.contains(film)) {
                    films.add(film);
                }
                userFilms.put(film, 1.0);
            }

            if (userFilms.size() > 0) {
                inputData.put(user.getId(), userFilms);
            }
        }

        // формирование матрицы различий
        for (HashMap<Film, Double> user : inputData.values()) {
            for (Map.Entry<Film, Double> e : user.entrySet()) {
                if (!diff.containsKey(e.getKey())) {
                    diff.put(e.getKey(), new HashMap<>());
                    freq.put(e.getKey(), new HashMap<>());
                }
                for (Map.Entry<Film, Double> e2 : user.entrySet()) {
                    int oldCount = 0;
                    if (freq.get(e.getKey()).containsKey(e2.getKey())) {
                        oldCount = freq.get(e.getKey()).get(e2.getKey()).intValue();
                    }
                    double oldDiff = 0.0;
                    if (diff.get(e.getKey()).containsKey(e2.getKey())) {
                        oldDiff = diff.get(e.getKey()).get(e2.getKey()).doubleValue();
                    }
                    double observedDiff = e.getValue() - e2.getValue();
                    freq.get(e.getKey()).put(e2.getKey(), oldCount + 1);
                    diff.get(e.getKey()).put(e2.getKey(), oldDiff + observedDiff);
                }
            }
        }
        for (Film j : diff.keySet()) {
            for (Film i : diff.get(j).keySet()) {
                double oldValue = diff.get(j).get(i).doubleValue();
                int count = freq.get(j).get(i).intValue();
                diff.get(j).put(i, oldValue / count);
            }
        }

        // формирование предсказания
        HashMap<Film, Double> uPred = new HashMap<Film, Double>();
        HashMap<Film, Integer> uFreq = new HashMap<Film, Integer>();
        for (Film j : diff.keySet()) {
            uFreq.put(j, 0);
            uPred.put(j, 0.0);
        }

        for (Map.Entry<Long, HashMap<Film, Double>> e : inputData.entrySet()) {
            for (Film j : e.getValue().keySet()) {
                for (Film k : diff.keySet()) {
                    double predictedValue = diff.get(k).get(j).doubleValue() + e.getValue().get(j).doubleValue();
                    double finalValue = predictedValue * freq.get(k).get(j).intValue();
                    uPred.put(k, uPred.get(k) + finalValue);
                    uFreq.put(k, uFreq.get(k) + freq.get(k).get(j).intValue());
                }
            }
            HashMap<Film, Double> clean = new HashMap<>();
            for (Film j : uPred.keySet()) {
                if (uFreq.get(j) > 0) {
                    clean.put(j, uPred.get(j).doubleValue() / uFreq.get(j).intValue());
                }
            }
            for (Film j : films) {
                if (e.getValue().containsKey(j)) {
                    clean.put(j, e.getValue().get(j));
                } else if (!clean.containsKey(j)) {
                    clean.put(j, -1.0);
                }
            }
            outputData.put(e.getKey(), clean);
        }

        if (outputData.size() == 0) {
            return new ArrayList<>();
        } else {
            return outputData.get(userId).entrySet().stream()
                    .filter(film -> film.getValue() == 0.0)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
    }
}
