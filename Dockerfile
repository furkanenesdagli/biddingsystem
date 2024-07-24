# Use a base image with JDK 21
FROM openjdk:21-jdk-slim as build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and POM file
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download Maven dependencies
RUN ./mvnw dependency:go-offline

# Copy the source code into the container
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Use a smaller base image for running the application
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
