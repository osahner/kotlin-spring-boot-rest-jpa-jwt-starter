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
ENTRYPOINT ["java", "-Djava.awt.headless=true", "-XX:MaxRAMPercentage=75", "-XX:+UseSerialGC", "-Xlog:gc", "-XshowSettings:vm", "-Dfile.encoding=UTF-8", "-Dspring.profiles.active=docker", "-jar", "/opt/app/api.jar"]

EXPOSE 8888/tcp
HEALTHCHECK --interval=60s --retries=5 --start-period=5s --timeout=10s CMD wget --no-verbose --tries=1 --spider localhost:8888/starter-test/actuator/health || exit 1
