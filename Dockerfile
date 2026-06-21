# Estágio 1: Build da aplicação usando Maven com JDK 23 oficial 🎯
FROM maven:3.9-amazoncorretto-23 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Execução do JAR otimizado com Java 23 Alpine 🎯
FROM eclipse-temurin:23-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar prosaude-api.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "prosaude-api.jar"]