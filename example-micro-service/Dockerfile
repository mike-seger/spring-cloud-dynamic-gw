# Use the official OpenJDK base image for Java 17
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Install curl and other useful tools
RUN apt-get update && apt-get install -y curl vim net-tools

# Copy the built jar file from the host to the container
COPY build/libs/micro-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application will run on
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expose the port your application will run on
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
