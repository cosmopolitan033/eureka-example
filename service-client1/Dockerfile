FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/service-client1-0.0.1-SNAPSHOT.jar service-client1.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "service-client1.jar"]
