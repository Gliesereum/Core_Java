FROM openjdk:8-alpine

MAINTAINER yvlasiuk

ADD ./socket-0.0.1.jar /gls/socket/

WORKDIR /gls/karma

ARG PROFILE
ARG PROFILE="docker"

CMD java -jar -d64 -server -XX:+UseG1GC -Xms200M -Xmx1536M -XX:MaxMetaspaceSize=1536M -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
./socket-0.0.1.jar