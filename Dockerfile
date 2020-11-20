FROM adoptopenjdk/openjdk11-openj9:alpine-slim
MAINTAINER Oliver Sahner <osahner@gmail.com>

ARG JAR_FILE
ARG SPRING_BOOT_VERSION

LABEL "org.springframework.boot"="${SPRING_BOOT_VERSION}"

RUN mkdir /opt/app
RUN addgroup -g 1001 -S spring && adduser -u 1001 -S spring -G spring
RUN chown spring:spring /opt/app
USER spring

ADD target/${JAR_FILE} /opt/app/api.jar
ENTRYPOINT ["java", "-Djava.awt.headless=true", "-Dfile.encoding=UTF-8", "-Dspring.profiles.active=docker", "-jar", "/opt/app/api.jar"]
EXPOSE 8888/tcp
