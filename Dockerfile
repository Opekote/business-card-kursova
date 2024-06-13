# Use a multi-stage build to minimize the final image size
# First stage: build the application
FROM maven:3.8.5-openjdk-17 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Second stage: run the application
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/business-card-0.0.1-SNAPSHOT.jar /app/business-card-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app/business-card-0.0.1-SNAPSHOT.jar"]
