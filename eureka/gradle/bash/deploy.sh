#!/usr/bin/env bash

docker stop eureka
docker rm eureka
docker rmi eureka

cd ~/eureka/project/eureka
bash -x gradlew buildDocker --no-daemon --stacktrace -Dprod -Pprofile=prod -x test
docker logs -f eureka