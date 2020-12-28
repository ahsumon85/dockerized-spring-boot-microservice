FROM openjdk:8
VOLUME /app/log
ADD target/micro-sales-service-0.0.1-SNAPSHOT.jar micro-sales-service-0.0.1-SNAPSHOT.jar
EXPOSE 8380
ENTRYPOINT ["java", "-jar", "micro-sales-service-0.0.1-SNAPSHOT.jar"]
