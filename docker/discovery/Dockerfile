FROM java:8-jre-alpine

MAINTAINER yvlasiuk

ADD ./discovery/build/libs/discovery-0.0.1-SNAPSHOT.jar /gls/discovery/

WORKDIR /gls/discovery

ARG PORT

EXPOSE ${PORT}

ARG PROFILE
ARG PROFILE="docker"
ARG HOST="localhost"

CMD java -jar -Dspring.profiles.active=${PROFILE} -Dserver.port=${PORT} -Deureka.instance.hostname=${HOST} ./discovery-0.0.1-SNAPSHOT.jar

