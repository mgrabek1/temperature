### README.md

# Temperature API

## Features

- Process temperature data from a CSV file.
- RESTful API for temperature data operations.
- Swagger integration for API documentation.
- Docker support for containerization.

## Requirements

- Java 17
- Maven 3.6.3 or later
- Docker (for containerization)

## Setup

### Configuration

The application configuration is managed through the `application.properties` file located in `src/main/resources`. The primary configuration properties include:

```properties
spring.application.name=temperature
server.port=8081
csv.file.path=example_file.csv
```

## Build and Run Locally

- Clone the repository:
```agsl
git clone <repository_url>
cd temperature
```
- Build the application:
```agsl
mvn clean package
```
- Run the application:
```agsl
java -jar target/temperature-0.0.1-SNAPSHOT.jar
```


## Docker

To build a Docker image for the Temperature API, use the provided Dockerfile.

- Build the Docker image:
```agsl
docker build -t temperature-api .
```
- Run the Docker container:
```agsl
docker run -p 8081:8081 temperature-api
```

## Access Swagger UI
```agsl
http://localhost:8081/swagger-ui/index.html
```