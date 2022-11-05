package ru.yandex.practicum.filmorate.storage.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.exceptions.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor
@Primary
public class DatabaseDirectorStorage implements DirectorStorage {

    private final DirectorDao directorDao;

    @Override
    public List<Director> findAllDirectors() {
        return directorDao.findAllDirectors();
    }

    @Override
    public Director findDirectorById(Long id) {
        Optional<Director> directorOpt = directorDao.findDirectorById(id);
        if (directorOpt.isPresent()) {
            var director = directorOpt.get();
            return director;
        }
        throw new DirectorNotFoundException(
                String.format("Режиссер с таким id %s не существует", id));
    }

    @Override
    public Director createDirector(Director director) {
        directorDao.saveDirector(director);
        log.debug("Режиссер {} создан.", director.getName());
        return director;



    }

    @Override
    public Director updateDirector(Director director) {
        findDirectorById(director.getId());
        director = directorDao.updateDirector(director);
        log.debug("Данные о режиссере {} обновлены.", director.getName());
        return director;
    }

    @Override
    public void deleteDirector(Long id) {
        directorDao.deleteDirector(id);
    }

    @Override
    public List<Director> findDirectorByFilmId(Long filmId) {
        return directorDao.findDirectorByFilmId(filmId);
    }
}
