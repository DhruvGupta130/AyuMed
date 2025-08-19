# Stage 1: Build the application with Maven + Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run with Java 21
FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=build /app/target/System-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]