# --- Stage 1: Build the application ---
# Use a base image with JDK 21 to build the application
# This change resolves the "not found" error for the JDK image.
FROM eclipse-temurin:21-jdk as build

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project directory, respecting the .dockerignore file.
COPY . .

# Give the Maven Wrapper script execute permissions
RUN chmod +x mvnw

# Build the application, creating an executable JAR file.
# The `spring-boot:repackage` command is used to make it a runnable JAR.
RUN ./mvnw clean package -DskipTests

# --- Stage 2: Create the final production image ---
# Use a smaller, production-ready JRE base image, also with Java 21
# This change resolves the "not found" error for the JRE image.
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copy the built JAR from the 'build' stage
COPY --from=build /app/target/mailGenerator-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port on which the application will run
EXPOSE 8080

# Define the command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
