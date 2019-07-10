#!/usr/bin/env bash

docker stop chat
docker rm chat
docker rmi chat

cd ~/chat/project/chat
bash -x gradlew buildDockerExtern --no-daemon --stacktrace -Dextern -Pprofile=extern -x test
docker logs -f chat
