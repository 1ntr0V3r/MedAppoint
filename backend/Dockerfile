# Build stage
FROM gradle:jdk17-alpine AS build
WORKDIR /workspace

# Copy the entire project (assuming build context is root)
COPY . .

# Build ONLY the backend project
# We use the installed gradle instead of ./gradlew to avoid permission issues
# We typically need to exclude the app module or just target :backend:bootJar
RUN gradle :backend:bootJar --no-daemon -x test

# Run stage
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# The jar is located in backend/build/libs because we built from root
COPY --from=build /workspace/backend/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
