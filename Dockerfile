FROM eclipse-temurin:17-jdk-focal

ARG JAR_FILE
ARG SPRING_BOOT_VERSION

LABEL org.opencontainers.image.authors="Oliver Sahner <osahner@gmail.com>"
LABEL org.springframework.boot="${SPRING_BOOT_VERSION}"

RUN mkdir /opt/app
RUN addgroup --system --gid 1001 spring
RUN adduser --system --no-create-home --uid 1001 --gid 1001 spring
RUN chown spring:spring /opt/app
USER spring

ADD target/${JAR_FILE} /opt/app/api.jar
ENTRYPOINT ["java", "-Djava.awt.headless=true", "-Dfile.encoding=UTF-8", "-Dspring.profiles.active=docker", "-jar", "/opt/app/api.jar"]

EXPOSE 8888/tcp
