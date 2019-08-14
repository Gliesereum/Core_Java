FROM adoptopenjdk/openjdk11:alpine-jre

MAINTAINER yvlasiuk

ADD ./mail-0.0.1.jar /gls/mail/

WORKDIR /gls/mail

ARG DEBUG_PORT

EXPOSE ${DEBUG_PORT}

CMD sleep 3m && java -jar -server -XX:+UseG1GC -Xms300M -Xmx1G -XX:MaxMetaspaceSize=1G -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:${DEBUG_PORT} \
./mail-0.0.1.jar
