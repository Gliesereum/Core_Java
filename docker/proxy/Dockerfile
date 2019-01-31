FROM openjdk:8-alpine

MAINTAINER yvlasiuk

ADD ./proxy-0.0.1.jar /gls/proxy/

WORKDIR /gls/proxy

ARG PORT

EXPOSE ${PORT}

ARG PROFILE
ARG PROFILE="docker"

ARG RABBIT_HOST
ARG RABBIT_PORT
ARG RABBIT_USER
ARG RABBIT_PASSWORD
ARG EUREKA_HOST
ARG EUREKA_PORT

CMD java -jar -d64 -server -XX:+UseG1GC -Xms500M -Xmx1536M -XX:MaxMetaspaceSize=500M -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
-Dspring.profiles.active=${PROFILE} \
-Dserver.port=${PORT} \
-Deureka.host=${EUREKA_HOST} \
-Deureka.port=${EUREKA_PORT} \
-Drabbit.host=${RABBIT_HOST} \
-Drabbit.port=${RABBIT_PORT} \
-Drabbit.user=${RABBIT_USER} \
-Drabbit.password=${RABBIT_PASSWORD} \
./proxy-0.0.1.jar
