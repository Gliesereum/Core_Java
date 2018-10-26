FROM java:8-jre-alpine

MAINTAINER yvlasiuk

ADD ./proxy/build/libs/proxy-0.0.1-SNAPSHOT.jar /gls/proxy/

WORKDIR /gls/proxy

ARG PORT

EXPOSE ${PORT}

ARG PROFILE
ARG PROFILE="docker"

ARG EUREKA_HOST
ARG EUREKA_PORT

CMD java -jar -Dspring.profiles.active=${PROFILE} -Dserver.port=${PORT} -Deureka.host=${EUREKA_HOST} -Deureka.port=${EUREKA_PORT} ./proxy-0.0.1-SNAPSHOT.jar
