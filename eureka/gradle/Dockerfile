FROM openjdk:8-jre-alpine

ENV SLEEP 1

ADD *.jar /app.jar

RUN sh -c 'touch /app.jar'
EXPOSE 8761
CMD echo "The app will run in ${SLEEP} seconds..." && \
    sleep ${SLEEP} && \
    java -Djava.security.egd=file:/dev/./urandom -jar /app.jar