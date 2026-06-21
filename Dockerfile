# Estágio 1: Build da aplicação usando Maven com JDK 23 oficial 🎯
FROM maven:3.9-amazoncorretto-23 AS build
WORKDIR /app

# OTIMIZAÇÃO: Copia apenas o pom.xml primeiro para aproveitar o cache de dependências do Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o restante do código fonte e builda o JAR final pulando os testes
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Execução do JAR otimizado com Java 23 Alpine 🎯
FROM eclipse-temurin:23-jre-alpine
WORKDIR /app

# Copia o JAR gerado no estágio anterior de forma limpa
COPY --from=build /app/target/*.jar prosaude-api.jar

# Define a porta padrão do Spring Boot
ENV PORT=8081
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "prosaude-api.jar"]