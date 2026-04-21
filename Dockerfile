# положим maven для сборки
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# копируем pom.xml и исходный код
COPY pom.xml .

# качаем зависимости
RUN mvn dependency:go-offline
COPY src ./src

# собираем проект без тестов (так как нет бд)
RUN mvn clean package -DskipTests

# кладем легкий образа
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# копируем готовый .jar файл из первого этапа
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]