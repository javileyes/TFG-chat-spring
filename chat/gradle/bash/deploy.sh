#!/usr/bin/env bash

docker stop chat
docker rm chat
docker rmi chat

cd ~/chat/project/chat
bash -x gradlew buildDocker --no-daemon --stacktrace -Dprod -Pprofile=prod -x test
docker logs -f chat