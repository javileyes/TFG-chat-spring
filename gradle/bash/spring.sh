#!/usr/bin/env bash

mkdir /root/chat
mkdir /root/chat/git
mkdir /root/chat/mysql
mkdir /root/chat/storage

docker run --name chatdb -d -p 3306:3306 --restart always -v /root/chat/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=Vu6kFRmV -e MYSQL_DATABASE=chatdb mariadb --character-set-server=utf8 --collation-server=utf8_general_ci