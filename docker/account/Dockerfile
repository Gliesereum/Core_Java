FROM gradle:4.10.2-jdk8-alpine

ADD ./account /gls/account
ADD ./share /gls/share

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
ARG EUREKA_HOST
ARG EUREKA_PORT

EXPOSE 5005

USER root
CMD gradle clean
CMD gradle build && java -Xmx500m -agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n -jar -Dspring.profiles.active=${PROFILE} -Dserver.port=${PORT} -Deureka.host=${EUREKA_HOST} -Deureka.port=${EUREKA_PORT} -Ddb.host=${DB_HOST} -Ddb.port=${DB_PORT} -Ddb.username=${DB_USERNAME} -Ddb.password=${DB_PASSWORD} -Dredis.host=${REDIS_HOST} -Dredis.port=${REDIS_PORT} -Dredis.password=${REDIS_PASSWORD} ./build/libs/account-0.0.1-SNAPSHOT.jar
