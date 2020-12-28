FROM openjdk:8
VOLUME /app/log
ADD target/micro-item-service-0.0.1-SNAPSHOT.jar micro-item-service-0.0.1-SNAPSHOT.jar
EXPOSE 8280
ENTRYPOINT ["java", "-jar", "micro-item-service-0.0.1-SNAPSHOT.jar"]
