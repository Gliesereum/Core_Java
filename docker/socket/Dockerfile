FROM adoptopenjdk/openjdk11:alpine-jre

MAINTAINER yvlasiuk

ADD ./socket-0.1.0.jar /gls/socket/

WORKDIR /gls/socket

ARG PROFILE
ARG PROFILE="docker"

CMD java -jar -server -XX:+UseG1GC -Xms200M -Xmx1536M -XX:MaxMetaspaceSize=1536M -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
./socket-0.1.0.jar