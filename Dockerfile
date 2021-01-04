FROM adoptopenjdk:15-jre-hotspot

ADD target/slike-service.jar slike-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "slike-service.jar"]