FROM adoptopenjdk/openjdk11:alpine-jre

MAINTAINER vitalij

ADD ./karma-0.0.1.jar /gls/karma/

WORKDIR /gls/karma

ARG DEBUG_PORT

EXPOSE ${DEBUG_PORT}

CMD sleep 5m && java -jar -server -XX:+UseG1GC -Xms500M -Xmx1536M -XX:MaxMetaspaceSize=1536M -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:${DEBUG_PORT} \
./karma-0.0.1.jar