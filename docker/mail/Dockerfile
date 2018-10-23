FROM gradle:4.10.2-jdk8-alpine

ADD ./mail /gls/mail
ADD ./share /gls/share

WORKDIR /gls/mail

ARG PORT

EXPOSE ${PORT}

ARG PROFILE
ARG PROFILE="docker"

ARG REDIS_HOST
ARG REDIS_PORT
ARG REDIS_PASSWORD
ARG EUREKA_HOST
ARG EUREKA_PORT

USER root
CMD gradle clean
CMD gradle build && java -Xmx500m -jar -Dspring.profiles.active=${PROFILE} -Dserver.port=${PORT} -Deureka.host=${EUREKA_HOST} -Deureka.port=${EUREKA_PORT} -Dredis.host=${REDIS_HOST} -Dredis.port=${REDIS_PORT} -Dredis.password=${REDIS_PASSWORD} ./build/libs/mail-0.0.1-SNAPSHOT.jar
