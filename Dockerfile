# Compilation stage

# Uses an image with Maven + Java 21 to compile the project
# We set "build" name to re-use it after
FROM maven:3.9.8-amazoncorretto-21 AS build

# Set the working directory inside the container
# We name it as we want
WORKDIR /app

# Copy the Maven configuration file first to leverage Docker cache
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build the application and generate the JAR file
# Tests are skipped to speed up the build
RUN mvn clean package -DskipTests

# ----------------
# Execution stage

# Runs the compiled application using a lightweight JDK image
FROM amazoncorretto:21

# Set the working directory for the runtime container
# We name it as we want
WORKDIR /app

# Copy only the generated JAR from the build stage
# This JAR contains the compiled Spring Boot application and all its dependencies
COPY --from=build /app/target/authentication-0.0.1-SNAPSHOT.jar app.jar

# Expose the port where the Spring Boot application runs
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
