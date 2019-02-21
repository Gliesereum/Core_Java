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

CMD java -jar -d64 -server -XX:+UseG1GC -Xms500M -Xmx1536M -XX:MaxMetaspaceSize=1536M -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
./proxy-0.0.1.jar
