# Advertising Service

REST API для системы размещения частных объявлений.

> Сделано в рамках финального задания курса Java Base (СЕНЛА).

## ⚙️ Стек

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![Maven](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![JUnit5](https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)

## 🚀 Быстрый запуск

### Программное обеспечение

Для запуска приложения установите приложение Docker. Скачать можно на [официальном сайте](https://docs.docker.com/get-started/get-docker/).

Убедиться в том, что Docker корректно установлен, можно 

### Команды для запуска

- Клонируйте репозиторий:

```shell
git clone https://github.com/aglushkovsky/advertising-service.git
```

- Перейдите в корневую директорию рабочей директории, выполните команду:

```shell
docker compose up
```

## 📃 Документация API

Документация API генерируется автоматически и доступна по эндпоинтам:

OpenAPI Specification (OAS):
- `/v3/api-docs`

Swagger UI:
- `/swagger-ui/index.html`
- `/swagger-ui.html` (редиректит на `/swagger-ui/index.html`)

### Тестирование API

- Страница Swagger UI позволяет **использовать графический интерфейс** для отправки запросов.
- Для **авторизованных запросов** выполните аутентификацию. На странице Swagger UI нажмите кнопку `Authorize`
  и введите полученный JWT-токен в появившемся popup-окне.
