FROM openjdk:8-alpine

MAINTAINER yvlasiuk

ADD ./mail-0.0.1.jar /gls/mail/

WORKDIR /gls/mail

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

CMD java -jar -d64 -server -XX:+UseG1GC -Xms300M -Xmx1G -XX:MaxMetaspaceSize=1G -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
./mail-0.0.1.jar
