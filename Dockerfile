# Stage 1: build the jar using Maven
FROM maven:3.9.6-eclipse-temurin-21 as build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Stage 2: run with lightweight JRE
FROM eclipse-temurin:21-jre

WORKDIR /
COPY --from=build /app/target/most-active-cookie.jar app/app.jar

ENTRYPOINT ["java", "-jar", "app/app.jar"]
