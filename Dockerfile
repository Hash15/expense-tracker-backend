# Step 1: Build the JAR
FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /app
COPY . .
RUN mvn -q -DskipTests package

# Step 2: Run the JAR with a lightweight JDK image
FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
