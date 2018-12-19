FROM java:8-jre-alpine

MAINTAINER yvlasiuk

ADD ./account/build/libs/account-0.0.1.jar /gls/account/

WORKDIR /gls/account

ARG PORT

EXPOSE ${PORT}

ARG PROFILE
ARG PROFILE="docker"

ARG DB_HOST
ARG DB_PORT
ARG DB_USERNAME
ARG DB_PASSWORD
ARG REDIS_HOST
ARG REDIS_PORT
ARG REDIS_PASSWORD
ARG RABBIT_HOST
ARG RABBIT_PORT
ARG RABBIT_USER
ARG RABBIT_PASSWORD
ARG EUREKA_HOST
ARG EUREKA_PORT


CMD java -jar -Dspring.profiles.active=${PROFILE} -Dserver.port=${PORT} -Deureka.host=${EUREKA_HOST} -Deureka.port=${EUREKA_PORT} -Ddb.host=${DB_HOST} -Ddb.port=${DB_PORT} -Ddb.username=${DB_USERNAME} -Ddb.password=${DB_PASSWORD} -Dredis.host=${REDIS_HOST} -Dredis.port=${REDIS_PORT} -Dredis.password=${REDIS_PASSWORD} -Drabbit.host=${RABBIT_HOST} -Drabbit.port=${RABBIT_PORT} -Drabbit.user=${RABBIT_USER} -Drabbit.port=${RABBIT_PASSWORD} ./account-0.0.1.jar