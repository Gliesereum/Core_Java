FROM adoptopenjdk/openjdk11:alpine-jre

MAINTAINER yvlasiuk

ADD ./file-0.0.1.jar /gls/file/

WORKDIR /gls/file

ARG DEBUG_PORT

EXPOSE ${DEBUG_PORT}


CMD sleep 2m && java -jar -server -XX:+UseG1GC -Xms300M -Xmx1G -XX:MaxMetaspaceSize=1G -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:${DEBUG_PORT} \
./file-0.0.1.jar