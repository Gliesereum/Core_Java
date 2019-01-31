FROM openjdk:8-alpine

MAINTAINER vitalij

ADD ./karma-0.0.1.jar /gls/karma/

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
ARG ES_HOST
ARG ES_PORT
ARG EUREKA_HOST
ARG EUREKA_PORT


CMD java -jar -d64 -server -XX:+UseG1GC -Xms500M -Xmx1536M -XX:MaxMetaspaceSize=500M -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
-Dspring.profiles.active=${PROFILE} \
-Dserver.port=${PORT} \
-Deureka.host=${EUREKA_HOST} \
-Deureka.port=${EUREKA_PORT} \
-Ddb.host=${DB_HOST} \
-Ddb.port=${DB_PORT} \
-Ddb.username=${DB_USERNAME} \
-Ddb.password=${DB_PASSWORD} \
-Drabbit.host=${RABBIT_HOST} \
-Drabbit.port=${RABBIT_PORT} \
-Drabbit.user=${RABBIT_USER} \
-Drabbit.password=${RABBIT_PASSWORD} \
-Des.host=${ES_HOST} \
-Des.port=${ES_PORT} \
./karma-0.0.1.jar