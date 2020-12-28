## Spring Boot, Spring Cloud Oauth2, Eureka, Zuul, Hystrix Monitoring Dashboard, Swagger 3, JPA, MySQL

# Overview
The architecture is composed by five services:

   * [`micro-api-getway`](https://github.com/ahsumon85/advance-spring-boot-microservice#api-gateway-service): API Gateway created by **Zuul** that is internally uses Ribbon **Load Balancer**  and  also can monitor Hystrix stream from every API request by **Hystrix**

   * [`micro-eureka-server`](https://github.com/ahsumon85/advance-spring-boot-microservice#eureka-service): Service **Registry Server** created by Eureka with  **Load Balancer** for inter-service communication 

   * [`micro-auth-service`](https://github.com/ahsumon85/advance-spring-boot-microservice#authorization-service): Simple REST service created with `Spring Boot, Spring Cloud Oauth2, Spring Data JPA, MySQL` to use as an **authorization service**

   * [`micro-item-service`](https://github.com/ahsumon85/advance-spring-boot-microservice#item-service--resource-service): Simple REST service created with `Spring Boot, Spring Data JPA, MySQL and swagger to test api` to use as a **resource service**

   * [`micro-sales-service`](https://github.com/ahsumon85/advance-spring-boot-microservice#sales-service--resource-service): Simple REST service created with `Spring Boot, Spring Data JPA, MySQL and swagger to test api` to use as a **resource service**

* [`docker-deployment`](https://github.com/ahsumon85/advance-spring-boot-microservice#docker-deployment): Containerize deployment; Microservice docker deployment using docker, docker-compose

### tools you will need
* Maven 3.0+ is your build tool
* Your favorite IDE but we will recommend `STS-4-4.4.1 version`. We use STS.
* MySQL server
* JDK 1.8+

# Eureka Service

Eureka Server is an application that holds the information about all client-service applications. Every Micro service will register into the Eureka server and Eureka server knows all the client applications running on each port and IP address. Eureka Server is also known as Discovery Server.

## How to run eureka service?

### Build Project
Now, you can create an executable JAR file, and run the Spring Boot application by using the Maven or Gradle commands shown below −
For Maven, use the command as shown below −

`mvn clean install`

or

**Project import in sts4 IDE** 
```File > import > maven > Existing maven project > Root Directory-Browse > Select project form root folder > Finish```

### Run project 

After “BUILD SUCCESSFUL”, you can find the JAR file under the build/libs directory.
Now, run the JAR file by using the following command −

Run on terminal `java –jar <JARFILE> `

 Run on sts IDE

 `click right button on the project >Run As >Spring Boot App`

Eureka Discovery-Service URL: `http://localhost:8761`



# Authorization Service

An **Authorization Server** issues tokens to client applications on behalf of a **Resource** Owner for use in authenticating subsequent API calls to the **Resource Server**. The **Resource Server** hosts the protected **resources**, and can accept or respond to protected **resource** requests using access tokens.

## How to run auth service?

### Build Project
Now, you can create an executable JAR file, and run the Spring Boot application by using the Maven or Gradle commands shown below −
For Maven, use the command as shown below −

`mvn clean install`

or

**Project import in sts4 IDE** 
```File > import > maven > Existing maven project > Root Directory-Browse > Select project form root folder > Finish```

### Run project 

After “BUILD SUCCESSFUL”, you can find the JAR file under the build/libs directory.
Now, run the JAR file by using the following command −

Run on terminal `java –jar <JARFILE> `

 Run on sts IDE

 `click right button on the project >Run As >Spring Boot App`

After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `item-server` instance gate will be running on `8280` port

### Test Authorization Service
***Get Access Token***

Let’s get the access token for `admin` by passing his credentials as part of header along with authorization details of appclient by sending `client_id` `client_pass` `username` `userpsssword`

Now hit the POST method URL via POSTMAN to get the OAUTH2 token.

**`http://localhost:8180/oauth/token`**

Now, add the Request Headers as follows −

* `Authorization` − Basic Auth with your Client Id and Client secret.

* `Content Type` − application/x-www-form-urlencoded
  ![1](https://user-images.githubusercontent.com/31319842/95816138-2e40d180-0d40-11eb-99c7-403cdf7ef070.png)

Now, add the Request Parameters as follows −

* `grant_type` = password
* `username` = your username
* ` password` = your password
![2](https://user-images.githubusercontent.com/31319842/95816163-3bf65700-0d40-11eb-9c87-7b721e0a268f.png)

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



# Item Service -resource service

Now we will see `micro-item-service` as a resource service. The `micro-item-service` a REST API that lets you CRUD (Create, Read, Update, and Delete) products. It creates a default set of items when the application loads using an `ItemApplicationRunner` bean.

## Setting Up Swagger 2 with a Item Service 

To enable the Swagger2 in Spring Boot application, you need to add the following dependencies in our build configurations file.

```
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger2</artifactId>
	<version>2.9.2</version>
</dependency>
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-bean-validators</artifactId>
	<version>2.9.2</version>
</dependency>
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger-ui</artifactId>
	<version>2.9.2</version>
</dependency>
```

Now, add the `@EnableSwagger2` annotation in your main Spring Boot application. The `@EnableSwagger2` annotation is used to enable the `Swagger2` for your Spring Boot application. Here have two variable that has `clientId` and `clientSecret` value getting from `application.properties`  file

The code for main Spring Boot application is shown below −

```
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Value("${security.oauth2.client.client-id}")
	private String clientId;
	
	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;
	
	public static final String securitySchemaOAuth2 = "oauth2schema";
	public static final String authorizationScopeGlobal = "global";
	public static final String authorizationScopeGlobalDesc = "accessEverything";
}
```

**Swagger UI With an OAuth-Secured API**

Next, create Docket Bean to configure Swagger2 for your Spring Boot application. We need to define the base package to configure REST API(s) for Swagger2.

The Swagger UI provides a number of very useful features that we've covered well so far here. But we can't really use most of these if our API is secured and not accessible.

Let's see how we can allow Swagger to access an OAuth-secured API using the Authorization Code grant type in this example.

We'll configure Swagger to access our secured API using the *SecurityScheme* and *SecurityContext* support:

```
	@Bean
	public Docket itemsApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
			.apis(RequestHandlerSelectors.basePackage("com.ahasan.sales.controller"))
					.paths(PathSelectors.any()).build()
					.securityContexts(Collections.singletonList(securityContext()))
					.securitySchemes(Arrays.asList(securitySchema())).apiInfo(apiInfo());
	}
	
	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}
```

After defining the *Docket* bean, its *select()* method returns an instance of *ApiSelectorBuilder*, which provides a way to control the endpoints exposed by Swagger.

We can configure predicates for selecting *RequestHandler*s with the help of *RequestHandlerSelectors* and *PathSelectors*. Using *any()* for both will make documentation for our entire API available through Swagger.

**Security Configuration**

We'll define a *SecurityConfiguration* bean in our Swagger configuration and set some defaults:

```
@Bean
public SecurityConfiguration security() {
	return new SecurityConfiguration(clientId, clientSecret, "", "", "Bearer access 		token", ApiKeyVehicle.HEADER, HttpHeaders.AUTHORIZATION, "");
}
```

**SecurityScheme**

Next, we'll define our *SecurityScheme*; this is used to describe how our API is secured (Basic Authentication, OAuth2, …).

```
private OAuth securitySchema() {
		List<AuthorizationScope> authorizationScopeList = newArrayList();
		authorizationScopeList.add(new AuthorizationScope("READ", "read all"));
		authorizationScopeList.add(new AuthorizationScope("WRITE", "access all"));
//		authorizationScopeList.add(new AuthorizationScope("TRUSTED", "trusted all"));
		List<GrantType> grantTypes = newArrayList();
		GrantType passwordCredentialsGrant = new 		  ResourceOwnerPasswordCredentialsGrant("http://localhost:9191/auth-api/oauth/token");
		grantTypes.add(passwordCredentialsGrant);
		return new OAuth("oauth2", authorizationScopeList, grantTypes);
	}
```

Note that we used the Authorization Code grant type, for which we need to provide a token endpoint and the authorization URL of our OAuth2 Authorization Server.

And here are the scopes we need to have defined:

```
private List<SecurityReference> defaultAuth() {
		final AuthorizationScope[] authorizationScopes = new AuthorizationScope[2];
		authorizationScopes[0] = new AuthorizationScope("READ", "read all");
		authorizationScopes[1] = new AuthorizationScope("WRITE", "write all");
//		authorizationScopes[2] = new AuthorizationScope("TRUSTED", "trust all");
		return Collections.singletonList(new SecurityReference("oauth2", authorizationScopes));
	}
```

### Web Security paths configure 

Ignoring security for path related to Swagger functionalities:

````
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers("/v2/api-docs",
						"/swagger-resources/**",
						"/swagger-ui.html",
						"/webjars/**",
						"/swagger/**");
	}
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}
}
````



**Now we can test it in our browser by visiting:**

![Screenshot from 2020-12-07 15-41-54](https://user-images.githubusercontent.com/31319842/101335171-09319080-38a3-11eb-99d2-45972effff7b.png)

![Screenshot from 2020-12-07 15-42-08](https://user-images.githubusercontent.com/31319842/101335180-0afb5400-38a3-11eb-9167-4b9a64a9c491.png)

## How to run item service?

### Build Project
Now, you can create an executable JAR file, and run the Spring Boot application by using the Maven shown below −
For Maven, use the command as shown below −

**Project import in sts4 IDE** 
```File > import > maven > Existing maven project > Root Directory-Browse > Select project form root folder > Finish```

### Run project 

After “BUILD SUCCESSFUL”, you can find the JAR file under the build/libs directory.
Now, run the JAR file by using the following command −

 `java –jar <JARFILE> `
 Run on sts IDE
 `click right button on the project >Run As >Spring Boot App`

Eureka Discovery-Service URL: `http://localhost:8761`

After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `item-server` instance gate will be running on `8280` port

***Test HTTP GET Request on item-service -resource service***
```
curl --request GET 'localhost:8180/item-api/item/find' --header 'Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787'
```
* Here `[http://localhost:8180/item-api/item/find]` on the `http` means protocol, `localhost` for hostaddress `8180` are gateway service port because every api will be transmit by the   
  gateway service, `item-api` are application context path of item service and `/item/find` is method URL.

* Here `[Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787']` `Bearer` is toiken type and `62e2545c-d865-4206-9e23-f64a34309787` is auth service provided token


### For getting All API Information
On this repository we will see `secure-microservice-architecture.postman_collection.json` file, this file have to `import` on postman then we will ses all API information for testing api.


##
# Sales Service -resource service
Now we will see `micro-sales-service` as a resource service. The `micro-sales-service` a REST API that lets you CRUD (Create, Read, Update, and Delete) products. It creates a default set of items when the application loads using an `SalesApplicationRunner` bean.

## Setting Up Swagger 2 with a Item Service 

To enable the Swagger2 in Spring Boot application, you need to add the following dependencies in our build configurations file.

```
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger2</artifactId>
	<version>2.9.2</version>
</dependency>
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-bean-validators</artifactId>
	<version>2.9.2</version>
</dependency>
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger-ui</artifactId>
	<version>2.9.2</version>
</dependency>
```

Now, add the `@EnableSwagger2` annotation in your main Spring Boot application. The `@EnableSwagger2` annotation is used to enable the `Swagger2` for your Spring Boot application. Here have two variable that has `clientId` and `clientSecret` value getting from `application.properties`  file

The code for main Spring Boot application is shown below −

```
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Value("${security.oauth2.client.client-id}")
	private String clientId;
	
	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;
	
	public static final String securitySchemaOAuth2 = "oauth2schema";
	public static final String authorizationScopeGlobal = "global";
	public static final String authorizationScopeGlobalDesc = "accessEverything";
}
```

**Swagger UI With an OAuth-Secured API**

Next, create Docket Bean to configure Swagger2 for your Spring Boot application. We need to define the base package to configure REST API(s) for Swagger2.

The Swagger UI provides a number of very useful features that we've covered well so far here. But we can't really use most of these if our API is secured and not accessible.

Let's see how we can allow Swagger to access an OAuth-secured API using the Authorization Code grant type in this example.

We'll configure Swagger to access our secured API using the *SecurityScheme* and *SecurityContext* support:

```
	@Bean
	public Docket salesApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
			.apis(RequestHandlerSelectors.basePackage("com.ahasan.sales.controller"))
					.paths(PathSelectors.any()).build()
					.securityContexts(Collections.singletonList(securityContext()))
					.securitySchemes(Arrays.asList(securitySchema())).apiInfo(apiInfo());
	}
	
	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}
```

After defining the *Docket* bean, its *select()* method returns an instance of *ApiSelectorBuilder*, which provides a way to control the endpoints exposed by Swagger.

We can configure predicates for selecting *RequestHandler*s with the help of *RequestHandlerSelectors* and *PathSelectors*. Using *any()* for both will make documentation for our entire API available through Swagger.

**Security Configuration**

We'll define a *SecurityConfiguration* bean in our Swagger configuration and set some defaults:

```
@Bean
public SecurityConfiguration security() {
	return new SecurityConfiguration(clientId, clientSecret, "", "", "Bearer access 		token", ApiKeyVehicle.HEADER, HttpHeaders.AUTHORIZATION, "");
}
```

**SecurityScheme**

Next, we'll define our *SecurityScheme*; this is used to describe how our API is secured (Basic Authentication, OAuth2, …).

```
private OAuth securitySchema() {
		List<AuthorizationScope> authorizationScopeList = newArrayList();
		authorizationScopeList.add(new AuthorizationScope("READ", "read all"));
		authorizationScopeList.add(new AuthorizationScope("WRITE", "access all"));
//		authorizationScopeList.add(new AuthorizationScope("TRUSTED", "trusted all"));
		List<GrantType> grantTypes = newArrayList();
		GrantType passwordCredentialsGrant = new 		  ResourceOwnerPasswordCredentialsGrant("http://localhost:9191/auth-api/oauth/token");
		grantTypes.add(passwordCredentialsGrant);
		return new OAuth("oauth2", authorizationScopeList, grantTypes);
	}
```

Note that we used the Authorization Code grant type, for which we need to provide a token endpoint and the authorization URL of our OAuth2 Authorization Server.

And here are the scopes we need to have defined:

```
private List<SecurityReference> defaultAuth() {
		final AuthorizationScope[] authorizationScopes = new AuthorizationScope[2];
		authorizationScopes[0] = new AuthorizationScope("READ", "read all");
		authorizationScopes[1] = new AuthorizationScope("WRITE", "write all");
//		authorizationScopes[2] = new AuthorizationScope("TRUSTED", "trust all");
		return Collections.singletonList(new SecurityReference("oauth2", authorizationScopes));
	}
```

### Web Security paths configure 

Ignoring security for path related to Swagger functionalities:

````
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers("/v2/api-docs",
						"/swagger-resources/**",
						"/swagger-ui.html",
						"/webjars/**",
						"/swagger/**");
	}
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}
}
````

**Now we can test it in our browser by visiting:**

`http://localhost:8180/sales-api/swagger-ui.html`

![Screenshot from 2020-12-07 15-22-21](https://user-images.githubusercontent.com/31319842/101333238-93c4c080-38a0-11eb-8f06-02c29558a31b.png)

<img src="https://user-images.githubusercontent.com/31319842/101333236-932c2a00-38a0-11eb-8bdb-bde71fa98a7f.png" alt="Screenshot from 2020-12-07 15-22-53" style="height:%" />

## How to run sales service?

### Build Project
Now, you can create an executable JAR file, and run the Spring Boot application by using the Maven shown below −
For Maven, use the command as shown below −

**Project import in sts4 IDE** 
```File > import > maven > Existing maven project > Root Directory-Browse > Select project form root folder > Finish```

### Run project 

After “BUILD SUCCESSFUL”, you can find the JAR file under the build/libs directory.
Now, run the JAR file by using the following command −

 `java –jar <JARFILE> `
 Run on sts IDE
 `click right button on the project >Run As >Spring Boot App`

Eureka Discovery-Service URL: `http://localhost:8761`

After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `sales-server` instance gate will be run on `http://localhost:8280` port

#### Test HTTP GET Request on resource service -resource service
```
curl --request GET 'localhost:8180/sales-api/sales/find' --header 'Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787'
```
* Here `[http://localhost:8180/sales-api/sales/find]` on the `http` means protocol, `localhost` for hostaddress `8180` are gateway service port because every api will be transmit by the   
  gateway service, `sales-api` are application context path of item service and `/sales/find` is method URL.

* Here `[Authorization: Bearer 62e2545c-d865-4206-9e23-f64a34309787']` `Bearer` is toiken type and `62e2545c-d865-4206-9e23-f64a34309787` is auth service provided token


### For getting All API Information
On this repository we will see `secure-microservice-architecture.postman_collection.json` file, this file have to `import` on postman then we will ses all API information for testing api.



# API Gateway Service

Gateway Server is an application that transmit all API to desire services. every resource services information such us: `service-name, context-path` will beconfigured into the gateway service and every request will transmit configured services by gateway

### Hystrix configure on gateway service

Let's start by configuring hystrix monitoring dashboard on API Gateway Service application to view hystrix stream.

First, we need to add the `spring-cloud-starter-hystrix-dashboard` dependency:

```
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-hystrix-dashboard</artifactId>
	<version>1.4.7.RELEASE</version>
</dependency>
```

The main application class `ZuulApiGetWayRunner` to start Spring boot application.

```
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@EnableHystrixDashboard
public class ZuulApiGetWayRunner {

	public static void main(String[] args) {
		SpringApplication.run(ZuulApiGetWayRunner.class, args);
		System.out.println("Zuul server is running...");
	}

	@Bean
	public PreFilter preFilter() {
		return new PreFilter();
	}

	@Bean
	public PostFilter postFilter() {
		return new PostFilter();
	}

	@Bean
	public ErrorFilter errorFilter() {
		return new ErrorFilter();
	}

	@Bean
	public RouteFilter routeFilter() {
		return new RouteFilter();
	}
}
```

**@EnableHystrixDashBoard** – To give dashboard view of Hystrix stream.

**@EnableCircuitBreaker** – To enable Circuit breaker implementation.

**Zuul routes configuration** Open `application.properties` and add below entries-

```
#Set the Hystrix isolation policy to the thread pool
zuul.ribbon-isolation-strategy=thread

#each route uses a separate thread pool
zuul.thread-pool.use-separate-thread-pools=true
```

#### Hystrix dashboard view

* To **monitor via Hystrix dashboard**, open Hystrix dashboard at `http://localhost:8180/hystrix`

![Screenshot from 2020-12-07 12-44-38](https://user-images.githubusercontent.com/31319842/101318202-1ee68c00-388a-11eb-8170-ca519491db1f.png)



* Now view **hystrix stream** in dashboard – `http://localhost:8180/hystrix.stream`

![Screenshot from 2020-12-07 12-04-26](https://user-images.githubusercontent.com/31319842/101317913-9c5dcc80-3889-11eb-8cc1-c757788dfbbd.png)


## How to run API Gateway Service?

### Build Project
Now, you can create an executable JAR file, and run the Spring Boot application by using the Maven or Gradle commands shown below −
For Maven, use the command as shown below −

`mvn clean install`
or

**Project import in sts4 IDE** 
```File > import > maven > Existing maven project > Root Directory-Browse > Select project form root folder > Finish```

### Run project 

After “BUILD SUCCESSFUL”, you can find the JAR file under the build/libs directory.
Now, run the JAR file by using the following command −

 `java –jar <JARFILE> `

 Run on sts IDE

 `click right button on the project >Run As >Spring Boot App`

After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `zuul-server` on eureka dashboard. the gateway instance will be run on `http://localhost:8180` port

![Screenshot from 2020-11-15 11-21-33](https://user-images.githubusercontent.com/31319842/99894579-6af0d880-2caf-11eb-84aa-d41b16cfbd12.png)

After we seen start auth, sales, item, zuul instance then we can try `advance-microservice-architecture.postman_collection.json` imported API from postman with token



# Docker Deployment

Now we will show you how to Dockerize microservice.

Tested with

- Docker 19.03
- Ubuntu 19
- Java 8 or Java 11
- Maven

## Create Dockerfile for all services

**Done, create  .jar with Maven.**

```
$ cd advance-spring-boot-microservice
$ mvn clean install
```

**Run the Spring Boot application using terminal **

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



## Docker File

**FORM**

A `Dockerfile` is a text file, contains all the commands to assemble the docker image. Review the commands in the `Dockerfile`:

It creates a docker image base on `openjdk:8` and download `jdk` from Docker Hub This base image `openjdk:8` is just an example, we can find more base images from the official [Docker Hub](https://hub.docker.com/r/adoptopenjdk/openjdk11)

```
FROM openjdk:8
```

**VOLUME**

A volume is a persistent data stored it used to stored log file into the local directory in the define location 

```
VOLUME /app/log
```

**ADD**

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

**EXPOSE**

Writing `EXPOSE` in your Dockerfile, is merely a hint that a certain port is useful. Docker won’t do anything with that information by itself.

Defining a port as “exposed” doesn’t publish the port by itself.

```
EXPOSE 8380
```

**ENTRYPOINT**

Run the jar file with `ENTRYPOINT`.

`<instruction> [“executable”, “parameter”]`

`ENTRYPOINT ["echo", "Hello World"] (exec form)`

```
ENTRYPOINT ["java", "-jar", "X.X.0.1.jar"]
```



## Dockerizing the microservice

### Dockerizing the Eureka Service using `Dockerfile`

The content of the file itself can look something like this:

![Screenshot from 2020-12-08 11-00-07](https://user-images.githubusercontent.com/31319842/101447224-f96f8600-394e-11eb-95ec-3245652809a1.png)

#### Let's build the image using this Dockerfile. Move to the root directory of the application and run this command:

```
$ cd advance-spring-boot-microservice/micro-eureka-service/

$ pwd
/home/ahasan/advance-spring-boot-microservice/micro-eureka-service

$ ls
Dockerfile  pom.xml  src  target

$ docker build . -t eureka-server:0.1
```

#### Run docker eureka-server image

Start the docker container `eureka-server:0.1`, run the `micro-eureka-service/target/micro-eureka-service-0.0.1-SNAPSHOT.jar` file during startup.

- Add `run -d` to start the container in detach mode – run the container in the background.
- Add `run -p` to map ports.
- Add `run --name to create container name
- Add `eureka-server:0.1 ` image name

```
$ docker run -d \
	-p 8761:8761 \
	--name eureka \
	 eureka-server:0.1 
```



### Dockerizing the Authorization Service using `Dockerfile`

First of all we need to change the **database** connection details and **eureka** server ip  of the **application.properties**

- container name **i.e.eureka** instead of **localhost**. The **eureka** must be container name of eureka server

- container name **i.e.mysqldb** instead of **localhost**. The **mysqldb** must be container name of database server

```
spring:
  datasource:
    url: jdbc:mysql://mysqldb:3306/spring_rest?createDatabaseIfNotExist=true
    username: admin
    password: Ati@2020
 
eureka:
  client:
    service-url:
      defaultZone: http://eureka:8761/eureka/
  instance:
    prefer-ip-address: true
```

The content of the file itself can look something like this:

![Screenshot from 2020-12-08 11-00-13](https://user-images.githubusercontent.com/31319842/101447221-f8d6ef80-394e-11eb-8afc-4d2295dbc9a0.png)
#### Let's build the image using this Dockerfile. Move to the root directory of the application and run this command:

```
$ cd advance-spring-boot-microservice/micro-auth-service/

$ pwd
/home/ahasan/advance-spring-boot-microservice/micro-auth-service

$ ls
Dockerfile  pom.xml  src  target

$ docker build . -t auth-server:0.1
```

#### Run docker auth-server image

Start the docker container `auth-server:0.1`, run the `micro-auth-service/target/micro-auth-service-0.0.1-SNAPSHOT.jar` file during startup.

##### Run auth service with remote or local database

- Add `run --name`to create container name
- Add `run -p` to map ports.
- Add `run -v` to map stored log file into the local directory
- Add `run ---add-host` to connect remote database from container application 
- Add `run --link` to connect eureka server
- Add `run -d` to start the container in detach mode – run the container in the background.
- Add `auth-server:0.1 ` image name

```
$ docker run --name auth \
        -p 9191:9191 \
        -v /opt/docker/log:/app/log \
        --add-host mysqldb:192.168.0.33 \
        --link eureka:eureka \
        -d auth-server:0.1  
```

### Dockerizing the Item Service using `Dockerfile`

The content of the file itself can look something like this:

![Screenshot from 2020-12-08 10-59-49](https://user-images.githubusercontent.com/31319842/101447229-fbd1e000-394e-11eb-8686-e42a0bd1c1fc.png)
### Dockerizing the Sales Service using `Dockerfile`

The content of the file itself can look something like this:

![Screenshot from 2020-12-08 10-59-42](https://user-images.githubusercontent.com/31319842/101447230-fc6a7680-394e-11eb-9e61-3470c84fca3c.png)
### Dockerizing the Gateway Service using `Dockerfile`

The content of the file itself can look something like this:

![Screenshot from 2020-12-08 10-59-57](https://user-images.githubusercontent.com/31319842/101447226-fb394980-394e-11eb-81c7-4b72881d5ea5.png)


