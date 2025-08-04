# --- Stage 1: Build the application ---
# Use a base image with the JDK to build the application
FROM eclipse-temurin:17-jdk-focal as build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files, including the Maven Wrapper files
# This is the crucial change to fix the 'command not found' error
COPY pom.xml .
COPY mvnw .
COPY .mvn .
COPY src ./src

# Give the Maven Wrapper script execute permissions
RUN chmod +x mvnw

# Build the application, creating an executable JAR file.
RUN ./mvnw clean package -DskipTests

# --- Stage 2: Create the final production image ---
# Use a smaller, production-ready JRE base image
FROM eclipse-temurin:17-jre-focal

# Set the working directory
WORKDIR /app


# Copy the built JAR from the 'build' stage
COPY --from=build /app/target/mailGenerator-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port on which the application will run
EXPOSE 8080

# Define the command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
