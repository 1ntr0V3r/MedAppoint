FROM gradle:jdk17-alpine AS build
WORKDIR /home/gradle/src
COPY . .
# Force ignore the Android module if it tries to configure itself by just running the backend task directly
# and using --no-daemon. 
RUN gradle :backend:bootJar -x test --no-daemon

FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8080
COPY --from=build /home/gradle/src/backend/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
