FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/service-client2-0.0.1-SNAPSHOT.jar service-client2.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "service-client2.jar"]
