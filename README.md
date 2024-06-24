# Card Service REST API Docker Deployment

This project contains a simple Java Card Service REST API. This guide will help you deploy the API locally using Docker.

## Prerequisites

Make sure you have the following installed on your machine:

- [Docker](https://www.docker.com/get-started)

## Deploy using Docker

### 1. Clone the Repository

First, clone the repository to your local machine:

```bashn
git clone https://github.com/SergioBumer/cardService.git
git checkout master
```

### 2. Build the docker image

```bash
docker build -t card_service .
```

### 3. Run a container based on the image

```bash
docker run -d -p 8080:8080 card_service
```

This will make a detach deployment so you could be able to continue using your Terminal.

### 4. [Open the swagger docs](http://localhost:8080/swagger-ui/index.html)

The app is currently connected to an RDS MySQL database. Also you could run the project if you have Java 17 and Spring Boot 3 in your local environment.

