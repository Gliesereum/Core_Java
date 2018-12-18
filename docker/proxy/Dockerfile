FROM java:8-jre-alpine

MAINTAINER yvlasiuk

ADD ./proxy/build/libs/proxy-0.0.1.jar /gls/proxy/

WORKDIR /gls/proxy

ARG PORT

EXPOSE ${PORT}

ARG PROFILE
ARG PROFILE="docker"

ARG REDIS_HOST
ARG REDIS_PORT
ARG REDIS_PASSWORD
ARG EUREKA_HOST
ARG EUREKA_PORT

CMD java -jar -Dspring.profiles.active=${PROFILE} -Dserver.port=${PORT} -Deureka.host=${EUREKA_HOST} -Deureka.port=${EUREKA_PORT} -Dredis.host=${REDIS_HOST} -Dredis.port=${REDIS_PORT} -Dredis.password=${REDIS_PASSWORD} ./proxy-0.0.1.jar
