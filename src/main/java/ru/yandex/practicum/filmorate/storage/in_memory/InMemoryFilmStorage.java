package ru.yandex.practicum.filmorate.storage.in_memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> films = new HashMap<>();
    private Long filmCount = 1L;

    @Override
    public Film createFilm(Film film) {
        film.setId(getAndIncrement(film));

        films.put(film.getId(), film);
        log.debug("Фильм {} создан.", film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        findFilm(film.getId());
        films.put(film.getId(), film);
        log.debug("Данные о фильме {} обновлены.", film.getName());
        return film;
    }

    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findFilm(Long id) {
        var film = films.get(id);
        if (film != null) {
            return film;
        }
        throw new FilmNotFoundException(
                String.format("Пользователь с таким id %s не существует", id));
    }

    private Long getAndIncrement(Film film) {
        if (film.getId() != null) {
            return film.getId();
        }
        return filmCount++;
    }
}
