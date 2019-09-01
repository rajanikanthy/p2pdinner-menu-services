#!/bin/sh

while ! nc -z config-service 8888 ; do
    echo "Waiting for upcoming Config Server"
    sleep 2
done
java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
