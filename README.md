# Empezamos

* Git clone del proyecto raiz
* Abrir la carpeta raiz como un proyecto de intelliJ
* Abrir los modulos de eureka, gateway, etc como modulos gradle
* Activar el SDK de Java, el nivel lambda y los annotation processors

# Microservicios
Vamos a introducirnos en el mundo de los microservicios con esta primera aplicación.

Este tutorial es la base y el uso sencillo de microservicios. Como veremos en el proyecto de frutas
ya utilizamos otras librerias mas avanzadas como Feign, Hystrix, RabbitMQ y practicamente no vemos
absolutamente nada de Ribbon puesto que Eureka ya se encarga de balancear los microservicios a la
hora de pedirlos.

## Profiles
* Config: Tenemos el profile "prod" para usar el servidor de configuración con un repositorio externo o sin profile
para una configuración nativa desde la carpeta /native
* Eureka: tenemos sin profile al puerto 8010 o los tres profiles "eu", "na" y "ja" para el despliegue de 3 eurekas
con puertos 8010, 8011 y 8012

## Config Server - Config Client

### 1º Config Server
Creamos un servidor de configuracion
```
compile('org.springframework.cloud:spring-cloud-config-server')
@EnableConfigServer
spring:cloud:config:server:git:uri: https://gawenth@bitbucket.org/gawenth/configdata.git
server:port: 8001 (http://localhost:8001/configclient/default)
```

### 2º Creamos Repo
configclient.yml ([nombreaplicacioncliente.yml]) (nombre-profile.yml)
```
variable1: valor1, valor2, valor3
variable2: valor1, valor2, valor3
```
### 3º Config Client
Creamos una app que consuma ese servidor de configuracion
```
compile('org.springframework.cloud:spring-cloud-starter-config')
compile('org.springframework.boot:spring-boot-starter-web')
bootstrap.yml
spring:cloud:config:uri: http://localhost:8001
spring:application:name: ConfigClient (este nombre sirve para configclient.yml del git)
server:port: 8002
```

Hemos creado un servidor de configuración pero los datos nuevos que modificamos en el repositorio los recibe el configserver pero no el configclient a no ser que lo reiniciemos. Esto lo solucionaremos mas adelante usando un Spring Cloud Bus para de forma dinámica refrescar los cambios.

## Eureka - Microservicios

### 1º Eureka Server
creamos un servidor de discovery
```
compile('org.springframework.cloud:spring-cloud-starter-eureka-server')
@EnableEurekaServer
application.yml -> server:port: 8010
bootstrap.yml -> spring:application:name: eurekaserver
```

### 2º Microservicio
Creamos un servicio
```
compile('org.springframework.cloud:spring-cloud-starter-eureka')
compile('org.springframework.boot:spring-boot-starter-web')
@EnableDiscoveryClient
application.yml server:port: 0 eureka:client:serviceUrl:defaultZone:http://localhost:8010/eureka
bootstrap.yml spring:application:name: manzana
@GetMapping(“/port”) environment.getProperty(“local.server.port”);
```

### 3º Gateway
Creamos un servicio con puerto fijo
```
compile('org.springframework.cloud:spring-cloud-starter-eureka')
compile('org.springframework.boot:spring-boot-starter-web')
@EnableDiscoveryClient
application.yml server:port: 8020 eureka:client:serviceUrl:defaultZone:http://localhost:8010/eureka
bootstrap.yml spring:application:name: gateway
@GetMapping(“/manzanas”)
DiscoveryClient.getInstances(“manzana”).get(0).getUri() + “/port”
return new RestTemplate.getForObject(ruta, String.class)
```

Los microservicios se registran en el servidor de discovery (Eureka) y el servicio de gateway los localiza y puede utilizarlos. Ahora vamos a utilizar el anterior servidor de configuracion para decir cual es la url de eureka y que cada microservicio la enganche por su cuenta.
Para esto crearemos en el repositorio application.yml en vez del nombre concreto de cada microservicio. A cada microservicio le quitamos la config de eureka y le ponemos el spring:cloud:config:uri: http://localhost:8001

## Ribbon - LoadBalancer

Llegados a este punto lo mejor es integrar un balanceador de carga para poder tener n instancias Manzana y m instancias Pera. En nuestro caso vamos a usar Ribbon junto con Eureka para poder desplegar servicios con puertos random. Los microservicios se agruparán según el nombre de aplicación que usemos para desplegar la instancia. Tanto el gateway como el servicio deben de tener la dependencia de ribbon en gradle.

En vez de usar DiscoveryClient utilizaremos LoadBalancerClient.choose(“nombre”) y lo mas importante es declarar en el bootstrap.yml la siguiente instrucción para que Eureka pueda declarar dos instancias identicas del mismo servicio para que ribbon pueda balancear la carga:
```
eureka:instance:instanceId: ${spring.cloud.client.hostname}:${spring.application.name}:${spring.application.instance_id:${random.value}}
```

## Cassandra
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
* En el cliente web http://192.168.33.10:3000 creamos el keyspace
* El replication_factor sirve para fijar la cantidad de veces que queremos que los datos se persistan
* Es necesario tener al menos el mismo numero de nodos o mas que el replication_factor
* Si tenemos un RF de 1 y tenemos 2 o mas nodos y uno de estos se cae, no funcionaria por que los datos estan almacenados solo una vez
* Si tenemos un RF de 2 y tenemos 2 nodos y uno se cae, si funcionaria por que los datos estan replicados por igual
* Si tenemos un RF de 2 y tenemos 3 nodos y uno se cae, si funcionaria pero si se caen 2 no
* En resumen, podemos trabajar con (numeroNodos / 2) + 1 https://www.ecyrd.com/cassandracalculator/
```
CREATE KEYSPACE IF NOT EXISTS lobby WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '2'};
```
Para persistir en disco
```
-v /my/own/datadir:/var/lib/cassandra
```

## Datastax
Sistema de ficheros cluster que utiliza casandra por debajo
```
https://www.datastax.com/products/datastax-enterprise
```
