FROM gradle:7.2.0-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon

FROM openjdk:20-slim-buster

ENV spring.datasource.username=root
ENV spring.datasource.password=12345678
ENV DB_NAME=REST_Mock
ENV DB_HOST=base-datos
ENV PUERTO_APP=8080

EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/practica2.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/practica2.jar"]