#!/usr/bin/env bash
rm -rf ~/{} Riferimenti\ KB\ Cloud\ Aruba.txt

#CONFIGURO EL SERVICIO DE SSH PARA CONEXIÓN REMOTA Y SEGURA CON EL VPS
mkdir ~/.ssh
public_key=AAAAB3NzaC1yc2EAAAADAQABAAABAQCb6lWbfKtGzp4Qml1WiwLpTRx6JYbWjmJZh1mgeQO5HqehihWBzhLg+gv8Bq/DJf3hr51/wj2bTqvmyt/7DSNklafo+7aDglFt0KO3jQurwmb/Q72q8Gt2mFatlyM2oJi/eW0iT9e4b3yuzQxPjq5AOieBFPEuOFZZ5sw6ftKt1s27aMCB40tp+3s1l8kMqSdQTvCGJOmu9kesHf32O3hiOMYWMsbDsnru6wqVFpylOzX7Jer1rzx6LM4gVRNnFEkqyIDLhzB4H6JW5zHVQFYY1F7EgZyhdTtWt0+xpDD4ra7q6zQmhXSuC5TmrAj5uvxf2R97FvoqoP0rmU79f2/b
echo ssh-rsa $public_key javiergimenezmoya@Mac-mini-de-JAVIER.local > ~/.ssh/authorized_keys

#INSTALO JAVA PARA PODER COMPILAR LAS FUENTES DEL REPOSITORIO (PASO PREVIO A LA CREACIÓN DEL DOCKER)
yum -y update
yum -y install epel-release nmap git java-1.8.0-openjdk java-1.8.0-openjdk-devel

#CREAMOS JERAQUIA DE CARPETAS
mkdir /root/chat
mkdir /root/chat/git
mkdir /root/chat/mysql
mkdir /root/chat/storage

#INICIALIZO EL REPOSITORIO REMOTO
cd /root/chat/git
git init --bare

#INYECTO CODIGO DE AUTODESPLIEGUE PARA INTEGRACIÓN CONTINUA EN EL DISPARADOR POST-RECEIVE
#(QUE SE EJECUTA AL LLEGAR UN PUSH AL REPOSITORIO)
cd /root/chat/git/hooks
echo "
rm -rf ~/chat/project
git clone ~/chat/git ~/chat/project
bash -x ~/chat/project/chat/gradle/bash/deploy-extern.sh
" > post-receive

#DESCARGO DE LA PAGINA OFICIAL EL CONTROLADOR DE DOCKERS
curl -fsSL https://get.docker.com | sh
systemctl start docker
systemctl enable docker
docker network create chat #CREO UNA NETWORK DE DOCKER PARA QUE LOS DOCKERS DE ESTE VPS SE VEAN


#DESCARGO Y PONGO EN MARCHA DOCKER DE MYSQL (MARIADB)
docker pull mariadb
#docker pull phpmyadmin/phpmyadmin

#DESCARGO Y PONGO EN MARCHA DOCKER DE CASSANDRA
docker pull cassandra
docker pull delermando/docker-cassandra-web:v0.4.0
