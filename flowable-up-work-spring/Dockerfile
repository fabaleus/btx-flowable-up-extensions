FROM amazoncorretto:17
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

#docker build --platform linux/arm64 --build-arg JAR_FILE=target/flowable-up-work-spring-0.0.0-SNAPSHOT.jar -t myorg/myapp .
#mvn package spring-boot:repackage
#docker run \
#-e spring.datasource.url=jdbc:postgresql://host.docker.internal:5432/flowable \
#-it myorg/myapp

