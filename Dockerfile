FROM openjdk:11-jdk
VOLUME /tmp
ADD build/libs/gateway*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
