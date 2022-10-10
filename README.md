# java-filmorate

Бэкенд web-сервиса, который будет работать с фильмами, пользователями, оценками пользователей к фильмам,
возвращать топ фильмов, списки друзей.

### Реализованы следующие эндпоинты:

#### 1. Фильмы

* POST /films - создание фильма
* PUT /films - редактирование фильма
* GET /films - получение списка всех фильмов
* GET /films/{id} - получение информации о фильме по его id
* PUT /films/{id}/like/{userId} — поставить лайк фильму
* DELETE /films/{id}/like/{userId} — удалить лайк фильма
* GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
  Если значение параметра count не задано, возвращает первые 10.

#### 2. Пользователи

* POST /users - создание пользователя
* PUT /users - редактирование пользователя
* GET /users - получение списка всех пользователей
* GET /users/{id} - получение данных о пользователе по id
* PUT /users/{id}/friends/{friendId} — добавление в друзья
* DELETE /users/{id}/friends/{friendId} — удаление из друзей
* GET /users/{id}/friends — возвращает список друзей
* GET /users/{id}/friends/common/{otherId} — возвращает список друзей, общих с другим пользователем

### Валидация

Данные, которые приходят в запросе на добавление нового фильма или пользователя,
проходят проверку по следующим критериям:

#### 1. Фильмы

* название не может быть пустым
* максимальная длина описания — 200 символов
* дата релиза — не раньше 28 декабря 1895 года
* продолжительность фильма должна быть положительной

#### 2. Пользователи

* электронная почта не может быть пустой и должна быть электронной почтой (аннотация @Email)
* логин не может быть пустым и содержать пробелы
* имя для отображения может быть пустым — в таком случае будет использован логин
* дата рождения не может быть в будущем.

### Схема базы данных

Схема отображает отношения таблиц в базе данных:

* films - данные о фильмах (primary key - film_id, foreign key - mpa_rating_id)
* genre - названия жанров фильма (primary key - genre_id)
* mpa_rating - определяет возрастное ограничение для фильма (primary key - mpa_rating_id)
* films_likes - информация о лайках фильма и кто их поставил (primary key - film_id, foreign key - user_id)
* users - данные о пользователях (primary key - user_id)
* friends - содержит информации о статусе «дружбы» между двумя пользователями (primary key - user_id)
    * неподтверждённая — когда один пользователь отправил запрос на добавление другого пользователя в друзья,
    * подтверждённая — когда второй пользователь согласился на добавление.

![](https://github.com/DaryaSerova/java-filmorate/compare/main...БД_java-filmorate?expand=1)

### Примеры запросов:

#### 1. Пользователи

создание пользователя

```
INSERT INTO users (name, email, login, birthday)
VALUES ( ?, ?, ?, ? );
```

редактирование пользователя

```
UPDATE users
SET email = ?,
login = ?,
name = ?,
birthday = ?
WHERE user_id = ?
```

получение списка всех пользователей

```
SELECT *
FROM users
```

получение информации о пользователе по его id

```
SELECT *
FROM users
WHERE user_id = ?
```

добавление в друзья

```
INSERT IGNORE INTO friends (user_id, friend_id)
VALUES (?, ?)
```

удаление из друзей

```
DELETE
FROM friends
WHERE user_id = ? AND friend_id = ?
```

возвращает список пользователей, являющихся его друзьями

```
SELECT users.*
FROM users
INNER JOIN friends ON users.user_id = friends.friend_id
WHERE friends.user_id = ?
```

список друзей, общих с другим пользователем

```
SELECT users.*
FROM users
INNER JOIN friends ON users.user_id = friends.friend_id
WHERE friends.user_id = ?

INTERSECT

SELECT users.*
FROM users
INNER JOIN friends ON users.user_id = friends.friend_id
WHERE friends.user_id = ?
```

#### 2. Фильмы

создание фильма

```
INSERT INTO films (name, description, release_date, duration, mpa_rating_id)
VALUES (?, ?, ?, ?, ?)
```

редактирование фильма

```
UPDATE films
SET name = ?,
description = ?,
release_date = ?,
duration = ?,
mpa_rating_id = ?
WHERE film_id = ?
```

получение списка всех фильмов

```
SELECT films.*, mpa_rating.name, COUNT(films_likes.user_id) AS rate
FROM films
LEFT JOIN mpa_rating ON films.mpa_rating_id = mpa_rating.mpa_rating_id
LEFT JOIN films_likes ON films.film_id = films_likes.film_id
GROUP BY films.film_id
ORDER BY films.film_id
```

получение информации о фильме по его id

```
SELECT films.*, mpa_rating.name, COUNT(films_likes.user_id) AS rate
FROM films
LEFT JOIN mpa_rating ON films.mpa_rating_id = mpa_rating.mpa_rating_id
LEFT JOIN films_likes ON films.film_id = films_likes.film_id
WHERE films.film_id = ?
GROUP BY films.film_id
```

пользователь ставит лайк фильму

```
INSERT IGNORE INTO films_likes (film_id, user_id)
VALUES (?, ?)
```

пользователь удаляет лайк

```
DELETE
FROM films_likes
WHERE film_id = ? AND user_id = ?
```

возвращает список из первых count фильмов по количеству лайков

```
SELECT films.*, mpa_rating.name, COUNT(films_likes.user_id) AS rate
FROM films
LEFT JOIN mpa_rating ON films.mpa_rating_id = mpa_rating.mpa_rating_id
LEFT JOIN films_likes ON films.film_id = films_likes.film_id
GROUP BY films.film_id
ORDER BY rate DESC, films.film_id
LIMIT ?
```
