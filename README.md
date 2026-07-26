# Recommendations-MVP

MVP рекомендательного сервиса банка: выдача персонализированных рекомендаций по статическим и динамическим правилам, управление правилами, сбор статистики, кэширование результатов.

## Стек технологий
- Java 17
- Spring Boot 3.3.x
- Spring Web, Spring Data JPA, Spring JDBC
- PostgreSQL (production), H2 (in-memory / test)
- Liquibase (миграции БД)
- Caffeine (локальный кэш)
- OpenAPI (Swagger UI)
- Maven

## Краткое описание
Сервис предоставляет рекомендации клиентам банка на основе:
- Статических правил (зашиты в логику/константы)
- Динамических правил (управляются через API менеджером)

Поддерживает:
- Выдачу рекомендаций по UUID пользователя
- CRUD правил рекомендаций
- Сбор статистики по применению правил
- Сброс кэша внешней системой (actuator endpoint)

## Ссылки на документацию
- [Требования и Use Cases](docs/requirements.md)
- [Архитектура и диаграммы](docs/architecture.md)
- [REST API (OpenAPI)](http://localhost:8080/swagger-ui.html)
- [Инструкция по развертыванию](docs/deployment.md)

## Как быстро погрузиться
1. Ознакомьтесь с `docs/requirements.md` — там описаны все User Story и акторы.
2. Посмотрите `docs/architecture.md` — диаграммы компонентов и Activity Diagram выдачи рекомендаций.
3. Запустите локально по инструкции в `docs/deployment.md`.
4. Проверьте API в Swagger UI.
