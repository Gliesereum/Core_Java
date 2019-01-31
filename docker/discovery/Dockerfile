FROM openjdk:8-alpine

MAINTAINER yvlasiuk

ADD ./discovery/build/libs/discovery-0.0.1.jar /gls/discovery/

WORKDIR /gls/discovery

ARG PORT

EXPOSE ${PORT}

ARG PROFILE
ARG PROFILE="docker"
ARG HOST="localhost"

CMD java -jar -d64 -server -XX:+UseG1GC -Xms300M -Xmx1G -XX:MaxMetaspaceSize=300M -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
-Dspring.profiles.active=${PROFILE} \
-Dserver.port=${PORT} \
-Deureka.instance.hostname=${HOST} \
./discovery-0.0.1.jar

