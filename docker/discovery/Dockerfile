FROM adoptopenjdk/openjdk11:alpine-jre

MAINTAINER yvlasiuk

ADD ./discovery-0.1.2.jar /gls/discovery/

WORKDIR /gls/discovery

ARG PORT

EXPOSE ${PORT}

ARG PROFILE
ARG PROFILE="docker"
ARG HOST="localhost"

CMD java -jar -server -XX:+UseG1GC -Xms300M -Xmx1G -XX:MaxMetaspaceSize=1G -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
./discovery-0.1.2.jar

