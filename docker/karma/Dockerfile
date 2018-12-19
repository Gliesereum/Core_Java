FROM java:8-jre-alpine

MAINTAINER vitalij

ADD ./karma/build/libs/karma-0.0.1.jar /gls/karma/

WORKDIR /gls/karma

ARG PORT

EXPOSE ${PORT}

ARG PROFILE
ARG PROFILE="docker"

ARG DB_HOST
ARG DB_PORT
ARG DB_USERNAME
ARG DB_PASSWORD
ARG RABBIT_HOST
ARG RABBIT_PORT
ARG RABBIT_USER
ARG RABBIT_PASSWORD
ARG EUREKA_HOST
ARG EUREKA_PORT


CMD java -jar -Dspring.profiles.active=${PROFILE} -Dserver.port=${PORT} -Deureka.host=${EUREKA_HOST} -Deureka.port=${EUREKA_PORT} -Ddb.host=${DB_HOST} -Ddb.port=${DB_PORT} -Ddb.username=${DB_USERNAME} -Ddb.password=${DB_PASSWORD} -Drabbit.host=${RABBIT_HOST} -Drabbit.port=${RABBIT_PORT} -Drabbit.user=${RABBIT_USER} -Drabbit.password=${RABBIT_PASSWORD} ./karma-0.0.1.jar