# Stage 1: build the jar using Maven
FROM maven:3.9.6-eclipse-temurin-17 as build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Stage 2: run with lightweight JRE
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=build /app/target/most-active-cookie-1.0-SNAPSHOT.jar ./app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
