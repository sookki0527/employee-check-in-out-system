FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/Api-Gateway-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
