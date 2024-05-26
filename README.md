### Тестовое задание для компании Effective Mobile
[![Maintainability](https://api.codeclimate.com/v1/badges/df4c05187ab8814f82a8/maintainability)](https://codeclimate.com/github/DEGTEVUWU/testTaskForEffectiveMobile/maintainability)

## Описание ##

Программа(компонент) для создания пользователя с уникальным банковским аккаунтом, телефонными номерами и электронными почтами. Для увеличения кол-ва денег на счете и переводов между счетами.

#### Текст тестового задания указан в самом конце файла

### Реализовано 
- класс по распознаванию текущего пользователя, чтоб избежать удаления или изменения пользователем данных других юзеров
- добавлены разные зависимые сущности для основного класса юзера - банк акккаунт, имеил, телефоонный номер
- добавлены разные виды обработчиков ошибок для круд операций
- добавлены дто сущности для приёма значений юзера и вывода значений и мапперы для преобразования в сущности
- добавлен круд-операций с юзером
- добавлены репозитории для сохранения всех сущностей
- добавлено шифрование пароля юзера
- добавлена проверка на логин, емаил и телефонные номера при создании юзера, нельзя создать нового юзера, если в базе есть юзер с какпими-либо совпадающими данными 
- добавлен патч-метод для изменения\добавления новых имейлов и тлф в репо и далее - в сущность юзера 
- добавлено удаление имейла и тлф по айди и по фактическому значению, с помощью перегрузки методов
- реализован фильтр для юзера по 6 параметрам - фио, дата рождения, тлф, имейл
- фильтр работает независимо от того указанны в нем все параметры, или некоторые из них пусты - поиск идет по заполненным
- реализована пагинация по 10 записей на странице
- реализован ассинхронный компонет, что увеличивает баланс юзера на 5% каждую минуту, не более 207% от нач депозита с помощью механизма Scheduled
- добавлен трансфер денег из авторизованного юзера в банк аккаунт другого юзера по айди, добавлены валидации - невозможность уменьшения баланса до отрицательных значений, проверка наличия обозначенныз банк аккаунтов, нельзя перевести отрицательную сумму
- добавлено логгирование с помощью slf4j в основные классы, сделано неск. лог-заглушек для демонстрации работы логгера
- добавлен сваггер в проект для создания и ведения api-документации, доступно в html-формате по адресу localhost:8080/swagger-ui/index.html
- добавлено тестирование на функционал трансфера денег
- для наглядности добавлено автоматическое создание и наполнение 25 юзеров с фейковыми данными, чтоб отключить это достаточно удалить\закомментировать класс `/component/DataInitializer`
- реализованы контроллеры для дочерних сущностей(банк аккаунт, имейл, тлф номер) и некоторые ендпоинты для них(просмотр всех сущностей и конкретной по id) - для возможного последующего расшируения функционала

### Использующиеся технологии ###
- Фреймворк: **Spring Boot**
- Аутентификация: **Spring Security**
- Соедение и работа с БД **Spring Data JPA**
- Автоматический маппинг: **Mapstruct**
- Шаблон проектирования: **DTO**
- Автоматическое наполнение данными **Datafaker**
- Тесты: **JUnit 5**, **Mockwebserver**
- Базы данных: **H2** | **PostgreSQL**
- Документация по API-приложения: **Springdoc Openapi**, **Swagger**



### Использование локально ###
##### В терминале: #####
```
git clone git@github.com:DEGTEVUWU/testTaskForEffectiveMobile.git
cd testTaskForEffectiveMobile
make spring 
```
##### В командной строке: #####
Для заведения нового юзера
```
curl -X POST "http://localhost:8080/api/users" \
    -H "Content-Type: application/json" \
    -d '{
        "login": "login",
        "password": "password",
        "balance": 100.00,
        "phoneNumber": ["12345"],
        "email": ["email@email.com"]
    }'
```
Для авторизации(получите токен в ответе - дальнейшие запросы доступны только с ним)
```
curl -X POST "http://localhost:8080/api/login" \
    -H "Content-Type: application/json" \
    -d '{
        "username": "login",
        "password": "password"
    }'
```
Для прочих запросов, используйте токен в запросе, например
```
curl "http://localhost:8080/api/users" \
    -H "Authorization: Bearer YOUR_TOKEN_HERE"
    
curl -X PUT "http://localhost:8080/api/users/1" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer YOUR_TOKEN_HERE" \
    -d '{
        "birthDate": "2000-01-01",
        "firstName": "Your_Name",
        "lastName": "Your_LastName",
        "surname": "Your_Surname"
    }'
    
curl -X POST "http://localhost:8080/api/users/1/bankAccount/transfer" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer YOUR_TOKEN_HERE" \
    -d '{
        "fromAccountId": 1,
        "toAccountId": 2,
        "amount": 50.00
    }'
```
##### В postman: #####
Набор основных команд
``` 
localhost:8080/api/users
localhost:8080/api/users/1/phone-numbers/1
localhost:8080/api/users?phoneNumberAll=&firstNameCont=&lastNameCont=&surnameCont=&emailAll=&birthDateGt=&pageNumber=1
localhost:8080/api/users/1/bankAccount/transfer
```

#### Дополнительная информация
Swagger доступен по адресу
``` 
localhost:8080/swagger-ui/index.html
```

#### Задание
``` 
Необходимо написать сервис для “банковских” операций. В нашей системе есть пользователи (клиенты), у каждого клиента есть строго один “банковский аккаунт”, в котором изначально лежит какая-то сумма. Деньги можно переводить между клиентами. На средства также начисляются проценты.

Функциональные требования:
В системе есть пользователи, у каждого пользователя есть строго один “банковский аккаунт”. У пользователя также есть телефон и email. Телефон и или email должен быть минимум один. На “банковском счету” должна быть какая-то изначальная сумма. Также у пользователя должна быть указана дата рождения и ФИО.
Для простоты будем считать что в системе нет ролей, только обычные клиенты. Пусть будет служебный апи (с открытым доступом), через который можно заводить новых пользователей в системе, указав логин, пароль, изначальную сумму, телефон и email (логин, телефон и email не должны быть заняты). 
Баланс счета клиента не может уходит в минус ни при каких обстоятельствах.
Пользователь может добавить/сменить свои номер телефона и/или email, если они еще не заняты другими пользователями.
Пользователь может удалить свои телефон и/или email. При этом нельзя удалить последний.
Остальные данные пользователь не может менять.
Сделать АПИ поиска. Искать можно любого клиента. Должна быть фильтрация и пагинация/сортировка. Фильтры:
Если передана дата рождения, то фильтр записей, где дата рождения больше чем переданный в запросе.
Если передан телефон, то фильтр по 100% сходству.
Если передано ФИО, то фильтр по like форматом ‘{text-from-request-param}%’
Если передан email, то фильтр по 100% сходству. 
Доступ к АПИ должен быть аутентифицирован (кроме служебного апи для создания новых клиентов).
Раз в минуту баланс каждого клиента увеличиваются на 5% но не более 207% от начального депозита.
Например:
Было: 100, стало: 105.
Было: 105, стало:110.25.
Реализовать функционал перевода денег с одного счета на другой. Со счета аутентифицированного пользователя, на счёт другого пользователя. Сделать все необходимые валидации и потокобезопасной.


Нефункциональные требования:
Добавить OpenAPI/Swagger
Добавить логирование
Аутентификация через JWT.
Нужно сделать тесты на покрытие функционала трансфера денег.

Стек:
Java 17
Spring Boot 3
База данных PostgreSQL
Maven
REST API
Дополнительные технологии (Redis, ElasticSearch и т.д.) на ваше усмотрение.
Фронтенд не нужен

```
