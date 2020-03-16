FROM adoptopenjdk:11-jre-openj9
MAINTAINER Oliver Sahner <osahner@gmail.com>
ARG JAR_FILE
RUN mkdir /opt/app
ADD target/${JAR_FILE} /opt/app/kotlin-spring-boot-rest-jpa-jwt-starter.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/opt/app/kotlin-spring-boot-rest-jpa-jwt-starter.jar"]
EXPOSE 4080/tcp
