FROM openjdk:11-ea-11-jre-slim
VOLUME /tmp
COPY build/libs/keytris-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]