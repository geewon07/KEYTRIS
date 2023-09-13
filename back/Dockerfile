FROM openjdk:11-ea-11-jre-slim
VOLUME /tmp
COPY ./keytris-0.0.1-SNAPSHOT.jar app.jar
ENV PROFILE local
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${PROFILE}" ,"app.jar"]