FROM openjdk:8
VOLUME /app/log
ADD target/micro-gateway-service-0.0.1-SNAPSHOT.jar micro-gateway-service-0.0.1-SNAPSHOT.jar
EXPOSE 8380
ENTRYPOINT ["java", "-jar", "micro-gateway-service-0.0.1-SNAPSHOT.jar"]
