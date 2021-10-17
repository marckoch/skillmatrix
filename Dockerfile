FROM openjdk:17-jdk-alpine
RUN addgroup -S springdocker && adduser -S springdocker -G springdocker
EXPOSE 8080
USER springdocker:springdocker
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]