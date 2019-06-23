#!/usr/bin/env bash

#BORRO FICHEROS PRECONFIGURADOS QUE VENÍAN CON EL VPS
rm -rf ~/{} Riferimenti\ KB\ Cloud\ Aruba.txt

#CONFIGURO EL SERVICIO DE SSH PARA CONEXIÓN REMOTA Y SEGURA CON EL VPS
mkdir ~/.ssh
public_key=AAAAB3NzaC1yc2EAAAADAQABAAABAQCb6lWbfKtGzp4Qml1WiwLpTRx6JYbWjmJZh1mgeQO5HqehihWBzhLg+gv8Bq/DJf3hr51/wj2bTqvmyt/7DSNklafo+7aDglFt0KO3jQurwmb/Q72q8Gt2mFatlyM2oJi/eW0iT9e4b3yuzQxPjq5AOieBFPEuOFZZ5sw6ftKt1s27aMCB40tp+3s1l8kMqSdQTvCGJOmu9kesHf32O3hiOMYWMsbDsnru6wqVFpylOzX7Jer1rzx6LM4gVRNnFEkqyIDLhzB4H6JW5zHVQFYY1F7EgZyhdTtWt0+xpDD4ra7q6zQmhXSuC5TmrAj5uvxf2R97FvoqoP0rmU79f2/b
echo ssh-rsa $public_key javiergimenezmoya@Mac-mini-de-JAVIER.local > ~/.ssh/authorized_keys
yum -y update
yum -y install epel-release nmap git java-1.8.0-openjdk java-1.8.0-openjdk-devel

#CREAMOS JERAQUIA DE CARPETAS
mkdir /root/chat
mkdir /root/chat/git
mkdir /root/chat/mysql
mkdir /root/chat/storage
cd /root/chat/git

#INICIALIZO EL REPOSITORIO REMOTO
git init --bare

#INYECTO CODIGO DE AUTODESPLIEGUE PARA INTEGRACIÓN CONTINUA EN EL DISPARADOR POST-RECEIVE
#(QUE SE EJECUTA AL LLEGAR UN PUSH AL REPOSITORIO)
cd /root/chat/git/hooks
echo "
rm -rf ~/chat/project
git clone ~/chat/git ~/chat/project
bash -x ~/chat/project/gradle/bash/deploy.sh
" > post-receive

#DESCARGO DE LA PAGINA OFICIAL EL CONTROLADOR DE DOCKERS
curl -fsSL https://get.docker.com | sh
systemctl start docker
systemctl enable docker

#DESCARGO Y PONGO EN MARCHA DOCKER DE MARIADB (MYSQL)
docker pull mariadb
docker run --name chatdb -d -p 3306:3306 --restart always -v /root/chat/mysql:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=Vu6kFRmV -e MYSQL_DATABASE=chatdb mariadb \
--character-set-server=utf8 --collation-server=utf8_general_ci
