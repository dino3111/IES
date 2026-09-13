FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY App.java .
COPY postgresql.jar .

RUN javac -cp .:postgresql.jar App.java

CMD ["java", "-cp", ".:postgresql.jar", "App"]