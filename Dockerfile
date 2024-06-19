# Use the Maven image with OpenJDK 17
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory
WORKDIR /app

# Copy the entire project directory into the image
COPY . .

# Build the application with Maven
RUN mvn clean package -DskipTests

# Stage 2: Create a minimal image to run the compiled Spring Boot application
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the packaged JAR file from the build stage to the new image
COPY --from=build /app/target/*.jar app.jar

# Expose the port that the Spring Boot application uses (default is 8080)
EXPOSE 8080

# Define the command to run your application
CMD ["java", "-jar", "app.jar"]