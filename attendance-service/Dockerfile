FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/attendance-service-1.0-SNAPSHOT.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
