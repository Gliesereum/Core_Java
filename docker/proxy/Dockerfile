FROM adoptopenjdk/openjdk11:alpine-jre

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

CMD sleep 5m && java -jar -server -XX:+UseG1GC -Xms500M -Xmx1536M -XX:MaxMetaspaceSize=1536M -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
./proxy-0.0.1.jar
