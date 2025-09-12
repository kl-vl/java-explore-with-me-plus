# Explore With Me - Сервис статистики


Многомодульная архитектура:
```
explore-with-me/
├── stats/                   # Сервис статистики
│   ├── stats-server/        # Web-сервер статистики (Web-приложение, Dockerfile, 9090 порт)
│   ├── stats-client/        # HTTP-клиент для работы с main-сервисом
│   └── stats-dto/           # Общие DTO классы
└── main-service/            # Основной сервис (8080 порт)
```

### Модули сервера статистики
- **stats/stats-server** - Web-сервер  для сбора и предоставления статистики
- **stats/stats-client** - HTTP-клиент для взаимодействия с сервером статистики
- **stats/stats-dto** - Общий формат данных (Data Transfer Objects)
