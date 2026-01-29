FROM gradle:jdk17-alpine AS build
WORKDIR /home/gradle/project
COPY . .
# Use the 'gradle' command installed in the image, NOT ./gradlew
# EXCLUDE the android app module to prevent errors
RUN gradle :backend:bootJar --no-daemon -x test

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
# Path might vary depending on how copy works, but usually it ends up here
COPY --from=build /home/gradle/project/backend/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
