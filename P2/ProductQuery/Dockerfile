FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve

COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests

# Expose the port on which the Spring Boot application will run (if applicable)
EXPOSE 8040

CMD ["java", "-jar", "-Dspring.profiles.active=mongoDB", "target/productquery-0.0.1-SNAPSHOT.jar"]