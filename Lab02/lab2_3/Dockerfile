FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /build/target/lab2_3-1.0-SNAPSHOT.jar app.jar
COPY alunos.csv ./alunos.csv
COPY init.sql ./init.sql
ENTRYPOINT ["java", "-jar", "app.jar"]
