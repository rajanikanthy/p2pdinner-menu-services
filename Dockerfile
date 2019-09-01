FROM openjdk:8-jdk-alpine
RUN apk --no-cache add netcat-openbsd
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
COPY p2pdinner-menu-service-entrypoint.sh .
RUN chmod 755 p2pdinner-menu-service-entrypoint.sh
ENTRYPOINT /p2pdinner-menu-service-entrypoint.sh
