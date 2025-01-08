FROM openjdk:17-jdk-slim

ENV PROJECT_HOME /usr/src/spring-nava-api
ENV JAR_NAME spring-nava-api.jar

WORKDIR $PROJECT_HOME

COPY target/$JAR_NAME $PROJECT_HOME/

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "spring-nava-api.jar"]
