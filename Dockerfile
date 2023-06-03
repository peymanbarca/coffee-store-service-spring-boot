FROM openjdk:8-jdk-alpine
RUN addgroup -S custom_usr && adduser -S custom_usr -G custom_usr
USER custom_usr:custom_usr
VOLUME /tmp


COPY ./target/coffee_store_service-0.0.1-SNAPSHOT.jar /app/coffee_store_service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/app/coffee_store_service-0.0.1-SNAPSHOT.jar"]