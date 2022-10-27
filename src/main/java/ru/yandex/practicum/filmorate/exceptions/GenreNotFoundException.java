package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class GenreNotFoundException extends RuntimeException {

    public GenreNotFoundException(final String message) {
        super(message);
    }

}
