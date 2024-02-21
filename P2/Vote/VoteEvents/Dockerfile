FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve

COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests


CMD ["java", "-jar", "-Dspring.profiles.active=mongoDB", "target/voteevents-0.0.1-SNAPSHOT.jar"]