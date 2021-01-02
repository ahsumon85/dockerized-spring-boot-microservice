# Docker, Docker-composer with [advance-spring-boot-microservice](https://github.com/ahsumon85/advance-spring-boot-microservice)
![1_cFEH7PQEJo7C9iRlGukWkg](https://user-images.githubusercontent.com/31319842/101862601-e6ea8c00-3b9c-11eb-8843-4bf612f97931.png)

## Overview
The architecture is composed by five services:

   * [`micro-api-getway`](https://github.com/ahsumon85/dockerized-spring-boot-microservice#api-gateway-service): Dockerizing **API Gateway Service** using docker and docker-compose. 

   * [`micro-eureka-server`](https://github.com/ahsumon85/dockerized-spring-boot-microservice#eureka-service): Dockerizing **Eureka Service** using docker and docker-compose. 

   * [`micro-auth-service`](https://github.com/ahsumon85/dockerized-spring-boot-microservice#authorization-service): Dockerizing **Authorization Service** using docker and docker-compose application connection with two deferent database  **local** **database** and **docker database**

   * [`micro-item-service`](https://github.com/ahsumon85/dockerized-spring-boot-microservice#item-service--resource-service):  Dockerizing **Item  Service** using docker and docker-compose application connection with two deferent database  **local** **database** and **docker database**

   * [`micro-sales-service`](https://github.com/ahsumon85/dockerized-spring-boot-microservice#sales-service--resource-service): Dockerizing **Sales  Service** using docker and docker-compose application connection with two deferent database  **local** **database** and **docker database**

##### [`microservice-docker-compose`](https://github.com/ahsumon85/dockerized-spring-boot-microservice#deployment-on-docker-compose): Dockerizing microservice using docker-compose apllication connect with local/remote database

### Tools you will need
* Maven 3.0+ is your build tool
* JDK 1.8
* Local MySQL Server and Dockerized MySQL Server
* Docker version 19
* docker-compose version 1.27.4

### Create Dockerfile for all services

**Create  .jar with Maven by build application**

```
$ cd dockerized-spring-boot-microservice

$ pwd
/home/ahasan/dockerized-spring-boot-microservice/

$ ls
micro-auth-service  micro-eureka-service  micro-gateway-service  micro-item-service  micro-sales-service  pom.xml

$ mvn clean install
```

**Run the Spring Boot application using terminal**

```
$ java -jar micro-eureka-service/target/micro-eureka-service-0.0.1-SNAPSHOT.jar 
$ java -jar micro-auth-service/target/micro-auth-service-0.0.1-SNAPSHOT.jar 
$ java -jar micro-item-service/target/micro-item-service-0.0.1-SNAPSHOT.jar
$ java -jar micro-sales-service/target/micro-sales-service-0.0.1-SNAPSHOT.jar
$ java -jar micro-gateway-service/target/micro-gateway-service-0.0.1-SNAPSHOT.jar
```

**Next, we containerize this application by adding a `Dockerfile`:** 

See the `Dockerfile` at the root of the project? We only need this `Dockerfile` text file to dockerize the Spring Boot application. now we will cover the two most commonly used approaches:

- **Dockerfile** – Specifying a file that contains native Docker commands to build the image
- **Maven** – Using a Maven plugin to build the image

Below we create simple `Dockerfile` under the project like as:

![Screenshot from 2020-12-08 11-29-59](https://user-images.githubusercontent.com/31319842/101444047-de9a1300-3948-11eb-92d5-f36f34244599.png)



### Docker File

#### FORM

A `Dockerfile` is a text file, contains all the commands to assemble the docker image. Review the commands in the `Dockerfile`:

It creates a docker image base on `openjdk:8` and download `jdk` from Docker Hub This base image `openjdk:8` is just an example, we can find more base images from the official [Docker Hub](https://hub.docker.com/r/adoptopenjdk/openjdk11)

```
FROM openjdk:8
```

#### VOLUME

A volume is a persistent data stored it used to stored log file into the local directory in the define location 

```
VOLUME /app/log
```

#### ADD

This tells Docker to copy files from the local file-system to a specific folder inside the build image. Here, we copy our `.jar` file to the build image inside `target/X.X.0.1.jar`

The `ADD` command requires a `source` and a `destination`.

If `source` is a file, it is simply copied to the `destination` directory.

```
ADD target/X.X.0.1.jar X.X.0.1.jar
```

- If `source` is a file, it is simply copied to the `destination` directory.
- If `source` is a directory, its *contents* are copied to the `destination`, but the directory itself is not copied.
- `source` can be either a tarball or a URL (as well).
- `source` needs to be within the directory where the `docker build` command was run.
- Multiple sources can be used in one `ADD` command.

#### EXPOSE

Writing `EXPOSE` in your Dockerfile, is merely a hint that a certain port is useful. Docker won’t do anything with that information by itself.

Defining a port as “exposed” doesn’t publish the port by itself.

```
EXPOSE 8380
```

#### ENTRYPOINT

Run the jar file with `ENTRYPOINT`.

`<instruction> [“executable”, “parameter”]`

`ENTRYPOINT ["echo", "Hello World"] (exec form)`

```
ENTRYPOINT ["java", "-jar", "X.X.0.1.jar"]
```



## Eureka Service

Eureka Server is an application that holds the information about all client-service applications. Every Micro service will register into the Eureka server and Eureka server knows all the client applications running on each port and IP address. Eureka Server is also known as Discovery Server.

### Dockerizing the Eureka Service using `Dockerfile`

The content of the file itself can look something like this:

![Screenshot from 2020-12-08 11-00-07](https://user-images.githubusercontent.com/31319842/101447224-f96f8600-394e-11eb-95ec-3245652809a1.png)

### Build the image using this Dockerfile. 

**Move to the root directory of the application and run this command:**

```
$ cd micro-eureka-service/

$ pwd
/home/ahasan/dockerized-spring-boot-microservice/micro-eureka-service

$ ls
Dockerfile  pom.xml  src  target

$ docker build . -t eureka-server:0.1
```

### Run docker eureka-server image

Start the docker container `eureka-server:0.1`, run the `micro-eureka-service/target/micro-eureka-service-0.0.1-SNAPSHOT.jar` file during startup.

- Add `run -d` to start the container in detach mode – run the container in the background.
- Add `run -p` to map ports.
- Add `run --name` to create container name
- Add `eureka-server:0.1 ` image name

```
$ docker run -d \
	-p 8761:8761 \
	--name eureka \
	 eureka-server:0.1 
```

**After successfully run then we will refresh  Eureka Discovery-Service URL: `http://localhost:8761`**



## Authorization Service

An **Authorization Server** issues tokens to client applications on behalf of a **Resource** Owner for use in authenticating subsequent API calls to the **Resource Server**. The **Resource Server** hosts the protected **resources**, and can accept or respond to protected **resource** requests using access tokens.

### Run the Spring Boot application using terminal 

```
$ java -jar micro-auth-service/target/micro-auth-service-0.0.1-SNAPSHOT.jar 
```

### Dockerizing the Authorization Service using `Dockerfile`

First of all we need to change the **database** connection details and **eureka** server ip  of the **application.properties**

- container name **i.e.eureka** instead of **localhost**. The **eureka** must be container name of eureka server

- hostname  **i.e.mysqldb** instead of **localhost** in url. The **mysqldb** must have database hostname

```
spring:
  datasource:
    url: jdbc:mysql://mysqldb:3306/spring_rest?createDatabaseIfNotExist=true
    username: [local_db_username]
    password: [local_db_password]
 
eureka:
  client:
    service-url:
      defaultZone: http://eureka:8761/eureka/
  instance:
    prefer-ip-address: true
```

The content of the file itself can look something like this:

![Screenshot from 2020-12-08 11-00-13](https://user-images.githubusercontent.com/31319842/101447221-f8d6ef80-394e-11eb-8afc-4d2295dbc9a0.png)

### Build the image using this Dockerfile. 

**Move to the root directory of the application and run this command:**

```
$ cd micro-auth-service/

$ pwd
/home/ahasan/dockerized-spring-boot-microservice/micro-auth-service

$ ls
Dockerfile  pom.xml  src  target

$ mvn clean install

$ docker build . -t auth-server:0.1
```

### Run docker auth-server image

Start the docker container `auth-server:0.1`, run the `micro-auth-service/target/micro-auth-service-0.0.1-SNAPSHOT.jar` file during startup.

### Run authorization service using docker

- Add `run --name`to create container name
- Add `run -p` to map ports.
- Add `run -v` to map stored log file into the local directory
- Add `run ---add-host` to connect remote database from container application 
- Add `run --link` to connect eureka container
- Add `run -d` to start the container in detach mode – run the container in the background.
- Add `auth-server:0.1 ` image name

#### Authorization service run with local/remote database


```
$ docker run --name auth \
        -p 9191:9191 \
        -v /opt/docker/log:/app/log \
        --add-host mysqldb:192.168.0.33 \
        --link eureka:eureka \
        -d auth-server:0.1  
```

After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `auth-server` instance gate will be running on `8280` port

### Test Authorization Service
#### Get Access Token

Let’s get the access token for `admin` by passing his credentials as part of header along with authorization details of appclient by sending `client_id` `client_pass` `username` `userpsssword`

Now hit the POST method URL via POSTMAN to get the OAUTH2 token.

**`http://localhost:9191/oauth/token`**

Now, add the Request Headers as follows −

* `Authorization` − Basic Auth with your `Client Id` and `Client secret`

* `Content Type` − application/x-www-form-urlencoded
 ![Screenshot from 2020-12-09 10-22-05](https://user-images.githubusercontent.com/31319842/101584943-ed47ff00-3a08-11eb-9d01-e196e0e089a6.png)

Now, add the Request Parameters as follows −

* `grant_type` = password
* `username` = your username
* ` password` = your password
![Screenshot from 2020-12-09 10-22-12](https://user-images.githubusercontent.com/31319842/101584942-ec16d200-3a08-11eb-9355-0e082a2493c7.png)

**HTTP POST Response**
```
{ 
  "access_token":"000ff762-414c-4605-858a-0ed7bee6f68e",
  "token_type":"bearer",
  "refresh_token":"79aabc70-f310-4c49-bf7e-516208b3bef4",
  "expires_in":999999,
  "scope":"read write"
}
```



## Item Service -resource service

Now we will see `micro-item-service` as a resource service. The `micro-item-service` a REST API that lets you CRUD (Create, Read, Update, and Delete) products. It creates a default set of items when the application loads using an `ItemApplicationRunner` bean.

### Run the Spring Boot application using terminal 

```
$ java -jar micro-item-service/target/micro-item-service-0.0.1-SNAPSHOT.jar
```

### Dockerizing the Item Service using `Dockerfile`

First of all we need to change the **database** connection and **eureka** server ip  of the **application.properties**

- container name **i.e.eureka** instead of **localhost**. The **eureka** must be container name of **eureka** server
- hostname  **i.e.itemdb** instead of **localhost** in url. The **itemdb** must have database hostname
- container name **i.e.auth** instead of **localhost** The **auth** must be container name of **auth** server

```
spring.datasource.url=jdbc:mysql://itemdb:3306/item_service
spring.datasource.username=[local_db_username]
spring.datasource.password=[local_db_password]
 
eureka.client.serviceUrl.defaultZone=http://eureka:8761/eureka/
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.preferIpAddress=true

security.oauth2.resource.token-info-uri=http://auth:9191/auth-api/oauth/check_token
security.oauth2.client.client-id=mobile
security.oauth2.client.client-secret=pin
```

The content of the file itself can look something like this:

![Screenshot from 2020-12-10 11-42-42](https://user-images.githubusercontent.com/31319842/101726649-26ea3a00-3add-11eb-84d2-a7f7537fa557.png)

### Build the image using this Dockerfile. 

**Move to the root directory of the application and run this command:**

```
$ cd micro-item-service/

$ pwd
/home/ahasan/dockerized-spring-boot-microservice/micro-item-service

$ ls
Dockerfile  pom.xml  src  target

$ mvn clean install

$ docker build . -t item-server:0.1
```

### Run docker item-server image

Start the docker container `item-server:0.1`, run the `micro-item-service/target/micro-item-service-0.0.1-SNAPSHOT.jar` file during startup.

### Run item service using docker

- Add `run --name`to create container name
- Add `run -p` to map ports.
- Add `run -v` to map stored log file into the local directory
- Add `run ---add-host` to connect remote database from container application 
- Add `run --link` to connect with **eureka** and **auth** container
- Add `run -d` to start the container in detach mode – run the container in the background.
- Add `item-server:0.1 ` image name

#### Item service run with local/remote database


```
$ docker run --name item \
        -p 8280:8280 \
        -v /opt/docker/log:/app/log \
        --add-host itemdb:192.168.0.33 \
        --link eureka:eureka \
        --link auth:auth \
        -d item-server:0.1  
```

After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `item-server` instance gate will be running on `8280` port

### Test HTTP GET Request on item-service -resource service
```
curl --request GET 'localhost:8280/item-api/item/find' --header 'Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787'
```
* Here `[http://localhost:8280/item-api/item/find]` on the `http` means protocol, `localhost` for hostaddress `8280` are sales service port, `item-api` are application context path of item service and `/item/find` is method URL.
  
* Here `[Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787']` `Bearer` is toiken type and `62e2545c-d865-4206-9e23-f64a34309787` is auth service provided token


#### For getting All API Information
On this repository we will see `docerized-microservice-architecture.postman_collection.json` file, this file have to `import` on postman then we will ses all API information for testing api.



## Sales Service -resource service
Now we will see `micro-sales-service` as a resource service. The `micro-sales-service` a REST API that lets you CRUD (Create, Read, Update, and Delete) products. It creates a default set of sales info when the application loads using an `SalesApplicationRunner` bean.

### Run the Spring Boot application using terminal 

```
$ java -jar micro-sales-service/target/micro-sales-service-0.0.1-SNAPSHOT.jar
```

### Dockerizing the sales Service using `Dockerfile`

First of all we need to change the **database** connection and **eureka** server ip  of the **application.properties**

- container name **i.e.eureka** instead of **localhost**. The **eureka** must be container name of **eureka** server
- hostname  **i.e.salesdb** instead of **localhost** in url. The **salesdb** must have database hostname
- container name **i.e.auth** instead of **localhost** The **auth** must be container name of **auth** server

```
spring.datasource.url=jdbc:mysql://salesdb:3306/sales_service
spring.datasource.username=[local_db_username]
spring.datasource.password=[local_db_password]
 
eureka.client.serviceUrl.defaultZone=http://eureka:8761/eureka/
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.preferIpAddress=true

security.oauth2.resource.token-info-uri=http://auth:9191/auth-api/oauth/check_token
security.oauth2.client.client-id=mobile
security.oauth2.client.client-secret=pin
```

The content of the file itself can look something like this:

![Screenshot from 2020-12-08 10-59-42](https://user-images.githubusercontent.com/31319842/101447230-fc6a7680-394e-11eb-9e61-3470c84fca3c.png)

### Build the image using this Dockerfile. 

**Move to the root directory of the application and run this command:**

```
$ cd micro-sales-service/

$ pwd
/home/ahasan/dockerized-spring-boot-microservice/micro-sales-service

$ ls
Dockerfile  pom.xml  src  target

$ mvn clean install

$ docker build . -t sales-server:0.1
```

### Run docker sales-server image

Start the docker container `sales-server:0.1`, run the `micro-sales-service/target/micro-sales-service-0.0.1-SNAPSHOT.jar` file during startup.

### Run sales service using docker

- Add `run --name`to create container name
- Add `run -p` to map ports.
- Add `run -v` to map stored log file into the local directory
- Add `run ---add-host` to connect remote database from container application 
- Add `run --link` to connect with **eureka** and **auth** container
- Add `run -d` to start the container in detach mode – run the container in the background.
- Add `sales-server:0.1 ` image name

#### Item service run with local/remote database


```
$ docker run --name sales \
        -p 8380:8380 \
        -v /opt/docker/log:/app/log \
        --add-host salesdb:192.168.0.33 \
        --link eureka:eureka \
        --link auth:auth \
        -d sales-server:0.1  
```

After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `sales-server` instance gate will be run on `http://localhost:8380` port

### Test HTTP GET Request on sales service -resource service

```
curl --request GET 'localhost:8380/sales-api/sales/find' --header 'Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787'
```
* Here `[http://localhost:8380/sales-api/sales/find]` on the `http` means protocol, `localhost` for hostaddress `8380` are sales service port, `sales-api` are application context path of item service and `/sales/find` is method URL.
  
* Here `[Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787']` `Bearer` is toiken type and `62e2545c-d865-4206-9e23-f64a34309787` is auth service provided token

#### For getting All API Information

On this repository we will see `dockerzide-microservice-architecture.postman_collection.json` file, this file have to `import` on postman then we will ses all API information for testing api.



## API Gateway Service

Gateway Server is an application that transmit all API to desire services. every resource services information such us: `service-name, context-path` will beconfigured into the gateway service and every request will transmit configured services by gateway

### Run the Spring Boot application using terminal 

```
$ java -jar micro-gateway-service/target/micro-gateway-service-0.0.1-SNAPSHOT.jar
```

### Dockerizing the gateway Service using `Dockerfile`

First of all we need to change the **eureka** server ip  in the **application.properties**

- container name **i.e.eureka** instead of **localhost**. The **eureka** must be container name of **eureka** server

```
eureka.client.serviceUrl.defaultZone=http://eureka:8761/eureka/
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.preferIpAddress=true
```

The content of the file itself can look something like this:

![Screenshot from 2020-12-10 12-28-32](https://user-images.githubusercontent.com/31319842/101729975-60be3f00-3ae3-11eb-8b72-62ef2d10d211.png)

### Build the image using this Dockerfile. 

**Move to the root directory of the application and run this command:**

```
$ cd micro-gateway-service/

$ pwd
/home/ahasan/dockerized-spring-boot-microservice/micro-gateway-service

$ ls
Dockerfile  pom.xml  src  target

$ mvn clean install

$ docker build . -t gateway-server:0.1
```

### Run docker gateway-server image

Start the docker container `sales-server:0.1`, run the `micro-gateway-service/target/micro-gateway-service-0.0.1-SNAPSHOT.jar` file during startup.

### Run gateway service using docker

- Add `run --name`to create container name
- Add `run -p` to map ports.
- Add `run -v` to map stored log file into the local directory
- Add `run --link` to connect with **eureka** and **auth** container
- Add `run -d` to start the container in detach mode – run the container in the background.
- Add `gateway-server:0.1 ` image name

#### Gateway service run


```
$ docker run --name gateway \
        -p 8180:8180 \
        -v /opt/docker/log:/app/log \
        --link eureka:eureka \
        -d gateway-server:0.1  
```



After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `gateway-server` on eureka dashboard. the gateway instance will be run on `http://localhost:8180` port

![Screenshot from 2020-11-15 11-21-33](https://user-images.githubusercontent.com/31319842/99894579-6af0d880-2caf-11eb-84aa-d41b16cfbd12.png)

After we seen start `auth, sales, item, zuul` instance then we can try `dockerized-microservice-architecture.postman_collection.json` imported API from postman with token. then we will pass request by `gateway-service` using `8180` port that is gateway port



### POST Request on auth service by gateway service

```
curl --location --request POST 'http://localhost:8180/auth-api/oauth/token' \
--header 'Authorization: Basic bW9iaWxlOnBpbg==' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'username=ahasan' \
--data-urlencode 'password=ahasan'
```

* Here `[http://localhost:8180/auth-api/oauth/token]` on the `http` means protocol, `localhost` for host address 8180 are gateway service port because every api will be transmit by the 
  gateway service, `auth-api` are application context path of item service and `/oauth/token` is method URL.

### GET Request on sales service by gateway service

```
curl --request GET 'localhost:8180/sales-api/sales/find' --header 'Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787'
```

* Here `[http://localhost:8180/sales-api/sales/find]` on the `http` means protocol, `localhost` for hostaddress 8180 are gateway service port because every api will be transmit by the 
  gateway service, `sales-api` are application context path of item service and `/sales/find` is method URL.
* Here `[Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787']` `Bearer` is toiken type and `62e2545c-d865-4206-9e23-f64a34309787` is auth service provided token

### GET Request on item-service by gateway service

```
curl --request GET 'localhost:8180/item-api/item/find' --header 'Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787'
```

* Here `[http://localhost:8280/item-api/item/find]` on the `http` means protocol, `localhost` for hostaddress `8180` are gateway service port because every api will be transmit by the   
  gateway service, `item-api` are application context path of item service and `/item/find` is method URL.

* Here `[Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787']` `Bearer` is toiken type and `62e2545c-d865-4206-9e23-f64a34309787` is auth service provided token



## Deployment on docker-compose 

### Remove running container of this name

```
$ dokcer rm -f eureka auth item sales gateway

$ dokcer rmi -f eureka-server:0.1 auth-server:0.1 item-server:0.1 
				sales-server:0.1 gateway-server:0.1
```

### Create `docker-compose` file in root directory of the application

```
version: "3.8"

services:

        eureka:
                build: ./micro-eureka-service/
                image: eureka-server:0.1
                container_name: eureka
                ports:
                        - "8761:8761"
                networks:
                        micro_network:
                                ipv4_address: 172.19.0.2
                deploy:
                        resources:
                                limits:
                                        cpus: '0.50'
                                        memory: 512M
        
        auth:
                build: ./micro-auth-service/
                image: auth-server:0.1
                container_name: auth
                ports:
                        - "9191:9191"
                extra_hosts:
                        - "mysqldb:192.168.0.33"
                links:
                        - eureka
                # depends_on:
                #         - eureka
                volumes:
                        - /opt/docker/log:/app/log
                networks:
                        micro_network:
                                ipv4_address: 172.19.0.3
                deploy:
                        resources:
                                limits:
                                        cpus: '0.50'
                                        memory: 1G

        item:
                build: ./micro-item-service/
                image: item-server:0.1
                container_name: item
                ports:
                        - "8280:8280"
                extra_hosts:
                        - "itemdb:192.168.0.33"
                links:
                        - eureka
                        - auth
                        - gateway
                volumes:
                        - /opt/docker/log:/app/log
                networks:
                        micro_network:
                                ipv4_address: 172.19.0.4
                deploy:
                        resources:
                                limits:
                                        cpus: '0.50'
                                        memory: 512M
        
        sales:
                build: ./micro-sales-service/
                image: sales-server:0.1
                container_name: sales
                ports:
                        - "8380:8380"
                extra_hosts:
                        - "salesdb:192.168.0.33"
                links:
                        - eureka
                        - auth
                        - gateway
                volumes:
                        - /opt/docker/log:/app/log
                networks:
                        micro_network:
                                ipv4_address: 172.19.0.5
                deploy:
                        resources:
                                limits:
                                        cpus: '0.50'
                                        memory: 512M

        gateway:
                build: ./micro-gateway-service/
                image: gateway-server:0.1
                container_name: gateway
                links:
                        - eureka
                ports:
                        - "8180:8180"
                networks:
                        micro_network:
                                ipv4_address: 172.19.0.6
                deploy:
                        resources:
                                limits:
                                        cpus: '0.50'
                                        memory: 512M
        
networks:
        micro_network:
                driver: bridge  
                ipam:
                        driver: default
                        config:
                                - subnet: "172.19.0.0/16"
    
```



- **`version "3.8"`:** This denotes that we are using version 3 of Docker Compose, and Docker will provide the appropriate features. At the time of writing this article, version 3.7 is latest version of Compose.
- **`services`:** This section defines all the different containers we will create. In our example, we have two services, web and database.
- **`eureka`:** This is the name of our Eureka container name.
- **`auth`:** This is the name of our authorization container name.
- **`item`:** This is the name of our Item container name.
- **`sales`:** This is the name of our Sales container name.
- **`gateway`:** This is the name of our Gateway container name.
- **`build`:** This specifies the location of our Dockerfile, and `.` represents the directory where the `docker-compose.yml` file is located.
- **`ports`:** This is used to map the container’s ports to the host machine.
- **`extra_hosts`** This is used to map local database from container 
- **`volumes`:** This is just like the `-v` option for mounting disks in Docker. In this example, we attach our code files directory to the containers’ `./code` directory. This way, we won’t have to rebuild the images if changes are made.
- **`links`:** This will link one service to another. For the bridge network, we must specify which container should be accessible to which container using links.
- **`image`:** If we don’t have a Dockerfile and want to run a service using a pre-built image, we specify the image location using the `image` clause. Compose will fork a container from that image.
- **`environment`:** The clause allows us to set up an environment variable in the container. This is the same as the `-e` argument in Docker when running a container.

### Build and Run the images using `docker-composer` 

**Move to the root directory of the application and run this command:**

```
$ cd dockerized-spring-boot-microservice/

$ pwd
/home/ahasan/dockerized-spring-boot-microservice

$ ls
micro-auth-service  micro-gateway-service  micro-sales-service  micro-eureka-service micro-item-service  pom.xml  docker-compose.yml

$ mvn clean install

$ docker-compose up
```

**To see the running containers logs on the terminal**

```
$ docker logs -f eureka

$ docker logs -f auth

$ docker logs -f item

$ dokcer logs -f sales

$ docker logs -f gateway
```

After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `gateway-server` on eureka dashboard. the gateway instance will be run on `http://localhost:8180` port

![Screenshot from 2020-11-15 11-21-33](https://user-images.githubusercontent.com/31319842/99894579-6af0d880-2caf-11eb-84aa-d41b16cfbd12.png)

After we seen start `auth, sales, item, zuul` instance then we can try `dockerized-microservice-architecture.postman_collection.json` imported API from postman with token. then we will pass request by `gateway-service` using `8180` port that is gateway port

### POST Request on auth service by gateway service

```
curl --location --request POST 'http://localhost:8180/auth-api/oauth/token' \
--header 'Authorization: Basic bW9iaWxlOnBpbg==' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'username=ahasan' \
--data-urlencode 'password=ahasan'
```

* Here `[http://localhost:8180/auth-api/oauth/token]` on the `http` means protocol, `localhost` for host address 8180 are gateway service port because every api will be transmit by the 
  gateway service, `auth-api` are application context path of item service and `/oauth/token` is method URL.

### GET Request on sales service by gateway service

```
curl --request GET 'localhost:8180/sales-api/sales/find' --header 'Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787'
```

* Here `[http://localhost:8180/sales-api/sales/find]` on the `http` means protocol, `localhost` for hostaddress 8180 are gateway service port because every api will be transmit by the 
  gateway service, `sales-api` are application context path of item service and `/sales/find` is method URL.
* Here `[Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787']` `Bearer` is toiken type and `62e2545c-d865-4206-9e23-f64a34309787` is auth service provided token

### GET Request on item-service by gateway service

```
curl --request GET 'localhost:8180/item-api/item/find' --header 'Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787'
```

* Here `[http://localhost:8280/item-api/item/find]` on the `http` means protocol, `localhost` for hostaddress `8180` are gateway service port because every api will be transmit by the   
  gateway service, `item-api` are application context path of item service and `/item/find` is method URL.

* Here `[Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787']` `Bearer` is toiken type and `62e2545c-d865-4206-9e23-f64a34309787` is auth service provided token

