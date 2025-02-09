# Используем базовый образ JDK 21
FROM gradle:8.9.0-jdk21-alpine AS build

# Копируем файлы проекта в контейнер
COPY . /app

# Устанавливаем рабочую директорию
WORKDIR /app

# Выполняем сборку проекта
RUN gradle build

# Используем базовый образ JDK 21 с Gradle для запуска проекта
FROM gradle:8.9.0-jdk21-alpine

# Копируем файлы проекта из контейнера сборки
COPY --from=build /app/build/libs/tgbot.gitlab-0.0.1-SNAPSHOT.jar /app/app.jar

# Устанавливаем рабочую директорию
WORKDIR /app

# Запускаем проект
CMD ["java", "-jar", "app.jar"]