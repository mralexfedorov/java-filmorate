package ru.yandex.practicum.filmorate.storage.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
@Primary
public class DatabaseGenreStorage implements GenreStorage {

    private final GenreDao genreDao;

    @Override
    public List<Genre> findAllGenres() {
        return genreDao.findAllGenres();
    }

    @Override
    public Genre findGenreById(Long id) {
        var genre = genreDao.findGenreById(id);
        if (genre.isPresent()) {
            return genre.get();
        }
        throw new GenreNotFoundException(
                String.format("Жанр с таким id %s не существует", id));
    }

    @Override
    public List<Genre> findGenreByFilmId(Long filmId) {
        return genreDao.findGenreByFilmId(filmId);
    }

}
