FROM adoptopenjdk/openjdk11-openj9:alpine-slim
MAINTAINER Oliver Sahner <osahner@gmail.com>

RUN mkdir /opt/app
RUN addgroup -g 1001 -S spring && adduser -u 1001 -S spring -G spring
RUN chown spring:spring /opt/app
USER spring

ARG jar_file

ADD ${jar_file} /opt/app/kotlin-spring-boot-rest-jpa-jwt-starter.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/opt/app/kotlin-spring-boot-rest-jpa-jwt-starter.jar"]
EXPOSE 4080/tcp
