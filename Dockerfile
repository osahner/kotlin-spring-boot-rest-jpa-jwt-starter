FROM amazoncorretto:17-alpine as corretto-jdk

# required for strip-debug to work
RUN apk add --no-cache binutils

# Build small JRE image
RUN jlink \
         --add-modules ALL-MODULE-PATH \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /jre

FROM alpine:latest
ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"

COPY --from=corretto-jdk /jre $JAVA_HOME

ARG JAR_FILE
ARG SPRING_BOOT_VERSION

LABEL org.opencontainers.image.authors="Oliver Sahner <osahner@gmail.com>"
LABEL org.springframework.boot="${SPRING_BOOT_VERSION}"

RUN mkdir /opt/app
RUN addgroup -g 1001 -S spring
RUN adduser -u 1001 -S spring -G spring
RUN chown spring:spring /opt/app
USER spring

ADD target/${JAR_FILE} /opt/app/api.jar
ENTRYPOINT ["java", "-Djava.awt.headless=true", "-XX:MaxRAMPercentage=75", "-XX:+UseSerialGC", "-Xlog:gc", "-XshowSettings:vm", "-Dfile.encoding=UTF-8", "-Dspring.profiles.active=docker", "-jar", "/opt/app/api.jar"]

EXPOSE 8888/tcp
HEALTHCHECK --interval=60s --retries=5 --start-period=5s --timeout=10s CMD wget --no-verbose --tries=1 --spider localhost:8888/starter-test/actuator/health || exit 1
