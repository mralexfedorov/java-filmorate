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
* GET /films/director/{directorId}?sortBy=[year,likes] - возвращает список фильмов выбранного режиссера, 
отсортированных либо по году релиза, либо по количеству лайков

#### 2. Пользователи

* POST /users - создание пользователя
* PUT /users - редактирование пользователя
* GET /users - получение списка всех пользователей
* GET /users/{id} - получение данных о пользователе по id
* PUT /users/{id}/friends/{friendId} — добавление в друзья
* DELETE /users/{id}/friends/{friendId} — удаление из друзей
* GET /users/{id}/friends — возвращает список друзей
* GET /users/{id}/friends/common/{otherId} — возвращает список друзей, общих с другим пользователем

#### 3. Режиссеры

* POST /directors - создание режиссера
* GET /directors - возвращает список всех режиссеров
* GET /directors/{id} - возвращает информацию о выбранном режиссеры
* PUT /directors — редактирование режиссера
* DELETE /directors/{id} - удаление режиссера



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

* film_t - данные о фильмах (primary key - id, foreign key - mpa_rating_id)
* genre_t - названия жанров фильма (primary key - id)
* film_genre_t - данные о жанрах какого-то фильма. У одного фильма может быть несколько жанров.
* film_director_t - данные о режиссерах какого-то фильма. У одного фильма может быть несколько режиссеров
* directors_t - имена режиссеров (primary key - id)
* mpa_rating_t - определяет возрастное ограничение для фильма (primary key - id)
* film_like_t - информация о лайках фильма и кто их поставил (primary key - id, foreign key - user_id, film_id)
* user_t - данные о пользователях (primary key - id)
* friendship_t - содержит информации о статусе «дружбы» между двумя пользователями (primary key - id)
    * status = true — в таблице две записи о дружбе двух пользователей (id1 = id2; id2 = id1),
    * status = false — в таблице одна запись о дружбе двух пользователей(id1 = id2).

![](https://github.com/DaryaSerova/java-filmorate/blob/add-database/БД_java-filmorate.png)


### Примеры запросов:

#### 1. Пользователи

создание пользователя

```
INSERT INTO user_t (name, email, login, birthday)
VALUES ( ?, ?, ?, ? );
```

редактирование пользователя

```
UPDATE user_t
SET email = ?,
login = ?,
name = ?,
birthday = ?
WHERE id = ?
```

получение списка всех пользователей

```
SELECT *
FROM user_t
```

получение информации о пользователе по его id

```
SELECT *
FROM user_t
WHERE id = ?
```

добавление в друзья

```
INSERT INTO friendship_t (user_id, friend_id, status)
VALUES (?, ?, ?)
```

удаление из друзей

```
DELETE
FROM friendship_t
WHERE user_id = ? AND friend_id = ?
```

возвращает список пользователей, являющихся его друзьями

```
SELECT ut.*
FROM friendship_t AS fst
INNER JOIN user_t AS ut ON ut.id = fst.friend_id
WHERE fst.user_id = ?
```

список друзей, общих с другим пользователем

```
SELECT ut.*
FROM user_t AS ut
INNER JOIN friendship_t AS fst ON ut.id = fst.friend_id
WHERE ut.id = ?

INTERSECT

SELECT ut.*
FROM user_t as ut
INNER JOIN friendship_t as fst ON ut.id = fst.friend_id
WHERE fst.user_id = ?
```

#### 2. Фильмы

создание фильма

```
INSERT INTO film_t (name, description, release_date, duration, mpa_rating_id)
VALUES (?, ?, ?, ?, ?)
```

редактирование фильма

```
UPDATE film_t
SET name = ?,
description = ?,
release_date = ?,
duration = ?,
mpa_rating_id = ?
WHERE id = ?
```

получение списка всех фильмов

```
SELECT ft.*, mpt.name, COUNT(flt.user_id) AS rate
FROM film_t AS ft
LEFT JOIN mpa_rating_t AS mpt ON ft.mpa_rating_id = mpt.id
LEFT JOIN film_like_t AS flt ON ft.id = flt.film_id
GROUP BY ft.id
ORDER BY ft.id
```

получение информации о фильме по его id

```
SELECT ft.*, mpt.name, COUNT(flt.user_id) AS rate
FROM film_t AS ft
LEFT JOIN mpa_rating_t AS mpt ON ft.mpa_rating_id = mpt.id
LEFT JOIN film_like_t AS flt ON ft.id = flt.film_id
WHERE ft.id = 2
GROUP BY ft.id
```

пользователь ставит лайк фильму

```
INSERT INTO film_like_t (film_id, user_id)
VALUES (?, ?)
```

пользователь удаляет лайк

```
DELETE
FROM film_like_t
WHERE film_id = ? AND user_id = ?
```

возвращает список из первых count фильмов по количеству лайков

```
SELECT ft.*, mpt.name, COUNT(flt.user_id) AS rate
FROM film_t AS ft
LEFT JOIN mpa_rating_t AS mpt ON ft.mpa_rating_id = mpt.id
LEFT JOIN film_like_t AS flt ON ft.id = flt.film_id
GROUP BY ft.id
ORDER BY rate DESC, ft.id
LIMIT ?
```
