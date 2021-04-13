FROM openjdk:15-jdk-alpine
VOLUME /tmp
ADD build/libs/gateway*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
