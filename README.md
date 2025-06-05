# 💰 Reactive Account Balance System

Простая система управления балансами и транзакциями на Spring Boot WebFlux с реактивным стеком, R2DBC и PostgreSQL.

---

## 🚀 Фунцкионал

* Создание баланса пользователя
  (POST /api/balances?name=**уникальное_имя_пользователя**)
* Просмотр баланса пользователя
  (GET /api/balances/{name})
* Создание транзакции
  (POST /api/balances/{name}/transactions)
  body (JSON):
    {
    "type": "deposit",
    "amount": 20,
    "currency_code": "USD"
    }
* Просмотр транзакций по балансу
  (GET /api/balances/{name}/transactions)

---

## 🧠 Примечания
* Все транзакции сохраняются в валюте, но пересчитываются в USD.
* Значение баланса хранится в USD и обновляется при каждой транзакции.
* Курс валют загружается из внешнего API.

## 📦 Стек технологий

- Java 17
- Spring Boot 3 (WebFlux, R2DBC, Validation)
- PostgreSQL
- Liquibase (миграции)
- Maven
- Docker
- JUnit + Mockito + Reactor Test

---

## ⚙️ Требования

- Docker (для запуска через контейнер)
- Java 17
- Maven 3.8+

---

## 🚀 Запуск приложения

### 🛠️ Вариант 1: Локальный запуск

#### 🔧 1. Конфигурация

Приложение ожидает переменные окружения следующего вида, которые будут переданы в файл `application.yml`:

```.env
SERVER_PORT=8080
DB_USERNAME=**пользователь_БД**
DB_PASSWORD=**пароль_от_БД**
R2DBC_DB_URL=r2dbc:postgresql://localhost:**порт_БД**/**имя_БД**
LB_DB_URL=jdbc:postgresql://localhost:**порт_БД**/**имя_БД**
API_KEY=**ключ_для_API_курсов_валют**
```

---

#### 🏃 2. Запуск приложения

```bash
./mvnw spring-boot:run
```

В браузере: http\://localhost:8080

---

### 🐳 Вариант 2: через Docker-Compose

#### 🔧 1. Конфигурация

Приложение ожидает переменные окружения следующего вида, которые будут переданы в файл `application.yml`:

```.env
SERVER_PORT=8080
APP_PORTS=8080:8080
DB_IMAGE=postgres:16.3
DB_PORTS=5050:5432
DB_USERNAME=**пользователь_БД**
DB_PASSWORD=**пароль_от_БД**
R2DBC_DB_URL=r2dbc:postgresql://**имя_контейнера_БД**/**имя_БД**?connectTimeout=20
LB_DB_URL=jdbc:postgresql://**имя_контейнера_БД**:**внутренний_порт_контейнера_БД**/**имя_БД**
API_KEY=**ключ_для_API_курсов_валют**
```

---

#### 🏃 2. Запуск приложения

Локальный запуск:

```bash
docker compose up -d   
```

---

## 🧱 Структура проекта

```
├── src/
│   ├── main/
│   │   ├── java/ge/accbalsystem/        # Код приложения: контроллеры, сервисы, модели, репозитории
│   │   └── resources/
│   │       ├── application.yml          # Конфигурация Spring Boot
│   │       └── db/
│   │           └── changelog/
│   │               └── initial/         # Миграции Liquibase (create-tables, seed-data)
│   └── test/
│       └── java/ge/accbalsystem/
│           └── service/                 # Unit-тесты (JUnit + Mockito + StepVerifier)
├── Dockerfile                           # Multi-stage: сборка jar → образ runtime
├── docker-compose.yml                   # PostgreSQL + Spring Boot в контейнерах
└── README.md                            # Инструкция по запуску


src/
├── main/
│   ├── java/ge.accbalsystem/    # код приложения
│   └── resources/db/changelog/  # миграции Liquibase
├── test/java/ge.accbalsystem/   # unit-тесты
```

---
