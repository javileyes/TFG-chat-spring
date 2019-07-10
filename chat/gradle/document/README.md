## Notas

```
docker run --name cassandra -d -p 9042:9042 -p 7000:7000 -e CASSANDRA_BROADCAST_ADDRESS=80.211.4.233 cassandra
docker run --name cassandra-web -d -p 3000:3000 -e CASSANDRA_HOST_IP=80.211.4.233 -e CASSANDRA_PORT=9042 --link cassandra delermando/docker-cassandra-web:v0.4.0
docker run --name mariadb -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=mariadb mariadb --character-set-server=utf8 --collation-server=utf8_general_ci
```

### Cassandra

En el cliente web http://192.168.33.10:3000 creamos el keyspace
El replication_factor sirve para fijar la cantidad de veces que queremos que los datos se persistan
Es necesario tener al menos el mismo numero de nodos o mas que el replication_factor
Si tenemos un RF de 1 y tenemos 2 o mas nodos y uno de estos se cae, no funcionaria por que los datos estan almacenados solo una vez
Si tenemos un RF de 2 y tenemos 2 nodos y uno se cae, si funcionaria por que los datos estan replicados por igual
Si tenemos un RF de 2 y tenemos 3 nodos y uno se cae, si funcionaria pero si se caen 2 no
En resumen, podemos trabajar con (numeroNodos / 2) + 1 https://www.ecyrd.com/cassandracalculator/
CREATE KEYSPACE IF NOT EXISTS chatdb WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '2'};

-v /my/own/datadir:/var/lib/cassandra
```
#!/usr/bin/env bash
#cassandra1
docker run --name cassandra -d -p 9042:9042 -p 7000:7000 -e CASSANDRA_BROADCAST_ADDRESS=80.211.4.233 cassandra
#cassandra2
docker run --name cassandra -d -p 9042:9042 -p 7000:7000 -e CASSANDRA_BROADCAST_ADDRESS=94.177.187.71 -e CASSANDRA_SEEDS=80.211.4.233 cassandra
#cassandra3
docker run --name cassandra -d -p 9042:9042 -p 7000:7000 -e CASSANDRA_BROADCAST_ADDRESS=94.177.199.128 -e CASSANDRA_SEEDS=80.211.4.233 cassandra
sleep 25
docker run --name cassandra-web -d -p 3000:3000 -e CASSANDRA_HOST_IP=80.211.4.233 -e CASSANDRA_PORT=9042 --link cassandra delermando/docker-cassandra-web:v0.4.0
```

```
docker run --name cassandra -d -p 9042:9042 -p 7000:7000 --restart always -e CASSANDRA_BROADCAST_ADDRESS=80.211.4.233 cassandra
docker run --name cassandra -d -p 9042:9042 -p 7000:7000 --restart always -e CASSANDRA_BROADCAST_ADDRESS=94.177.187.71 -e CASSANDRA_SEEDS=80.211.4.233 cassandra
docker run --name cassandra -d -p 9042:9042 -p 7000:7000 --restart always -e CASSANDRA_BROADCAST_ADDRESS=94.177.199.128 -e CASSANDRA_SEEDS=80.211.4.233 cassandra
docker run --name cassandra-web -d -p 3000:3000 --restart always -e CASSANDRA_HOST_IP=80.211.4.233 -e CASSANDRA_PORT=9042 --link cassandra delermando/docker-cassandra-web:v0.4.0
(80.211.4.233:3000 - Execute) CREATE KEYSPACE IF NOT EXISTS chatdb WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '2'};
(80.211.4.233:3000 - Execute) CREATE KEYSPACE IF NOT EXISTS chatdb_dev WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '2'};
```
