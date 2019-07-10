#!/usr/bin/env bash

docker stop gateway
docker rm gateway
docker rmi gateway

cd ~/gateway/project/gateway
bash -x gradlew buildDocker --no-daemon --stacktrace -Dprod -Pprofile=prod -x test
docker logs -f gateway