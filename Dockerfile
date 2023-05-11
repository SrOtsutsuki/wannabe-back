FROM openjdk:17.0.2-slim-buster
COPY target/wannabe-back.jar /usr/local/service/wannabe-back.jar
EXPOSE 8081:8081
ENTRYPOINT ["java","-jar","/usr/local/service/wannabe-back.jar"]