#!/usr/bin/env bash
rm -rf ~/{} Riferimenti\ KB\ Cloud\ Aruba.txt
mkdir ~/.ssh
echo ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCb6lWbfKtGzp4Qml1WiwLpTRx6JYbWjmJZh1mgeQO5HqehihWBzhLg+gv8Bq/DJf3hr51/wj2bTqvmyt/7DSNklafo+7aDglFt0KO3jQurwmb/Q72q8Gt2mFatlyM2oJi/eW0iT9e4b3yuzQxPjq5AOieBFPEuOFZZ5sw6ftKt1s27aMCB40tp+3s1l8kMqSdQTvCGJOmu9kesHf32O3hiOMYWMsbDsnru6wqVFpylOzX7Jer1rzx6LM4gVRNnFEkqyIDLhzB4H6JW5zHVQFYY1F7EgZyhdTtWt0+xpDD4ra7q6zQmhXSuC5TmrAj5uvxf2R97FvoqoP0rmU79f2/b javiergimenezmoya@Mac-mini-de-JAVIER.local > ~/.ssh/authorized_keys
yum -y update
yum -y install epel-release nmap git java-1.8.0-openjdk java-1.8.0-openjdk-devel
curl -fsSL https://get.docker.com | sh
systemctl start docker
systemctl enable docker
docker pull mariadb
docker pull phpmyadmin/phpmyadmin
docker pull cassandra
docker pull delermando/docker-cassandra-web:v0.4.0
