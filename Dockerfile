# # Stage 1: Build the JAR file
# FROM maven:3.8-alpine AS builder
# WORKDIR /app
# COPY pom.xml ./
# RUN mvn package

# # Stage 2: Create a slim image with the JAR file
# FROM openjdk:11-alpine

# # Copy the JAR file from the builder stage
# COPY --from=builder /app/target/*.jar /app/my-app.jar

# # Set the working directory and entry point command
# WORKDIR /app
# ENTRYPOINT ["java", "-jar", "/app/my-app.jar"]

# # Expose ports (if necessary)
# EXPOSE 8080  
# # Example port for a web application
FROM openjdk:11
EXPOSE 8080
COPY target/calma-apis-0.0.1-SNAPSHOT.jar /app/calma-apis-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "/app/calma-apis-0.0.1-SNAPSHOT.jar"]