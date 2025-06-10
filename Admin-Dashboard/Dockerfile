FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/Admin-Dashboard-1.0-SNAPSHOT.jar app.jar


EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
