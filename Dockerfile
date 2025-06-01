FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build with Maven Wrapper
RUN chmod +x mvnw && ./mvnw package -DskipTests

# Expose Spring Boot default port
EXPOSE 8080

# Default profile is 'prod', can be overridden at runtime
ENV SPRING_PROFILES_ACTIVE=prod

# Run the app
CMD ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "target/api-0.0.1-SNAPSHOT.jar"]