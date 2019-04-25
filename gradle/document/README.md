## Notas

```
docker run --name cassandra -d -p 9042:9042 -p 7000:7000 -e CASSANDRA_BROADCAST_ADDRESS=80.211.4.233 cassandra
docker run --name cassandra-web -d -p 3000:3000 -e CASSANDRA_HOST_IP=80.211.4.233 -e CASSANDRA_PORT=9042 --link cassandra delermando/docker-cassandra-web:v0.4.0
docker run --name mariadb -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=mariadb mariadb --character-set-server=utf8 --collation-server=utf8_general_ci
```

