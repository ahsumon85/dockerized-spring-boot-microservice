FROM openjdk:8
VOLUME /app/log
ADD target/micro-auth-service-0.0.1-SNAPSHOT.jar micro-auth-service-0.0.1-SNAPSHOT.jar
EXPOSE 9191
ENTRYPOINT ["java", "-jar", "micro-auth-service-0.0.1-SNAPSHOT.jar"]
