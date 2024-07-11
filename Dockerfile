FROM maven:3.6.3-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

COPY example_file.csv /app/example_file.csv

EXPOSE 8081

ENV csv.file.path=/app/example_file.csv

ENTRYPOINT ["java", "-jar", "app.jar"]
