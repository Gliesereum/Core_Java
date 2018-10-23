FROM gradle:4.10.2-jdk8-alpine

ADD ./proxy /gls/proxy
ADD ./share /gls/share

WORKDIR /gls/proxy

ARG PORT

EXPOSE ${PORT}

ARG PROFILE
ARG PROFILE="docker"

ARG EUREKA_HOST
ARG EUREKA_PORT

USER root
CMD gradle clean
CMD gradle build && java -Xmx500m -jar -Dspring.profiles.active=${PROFILE} -Dserver.port=${PORT} -Deureka.host=${EUREKA_HOST} -Deureka.port=${EUREKA_PORT} ./build/libs/proxy-0.0.1-SNAPSHOT.jar
