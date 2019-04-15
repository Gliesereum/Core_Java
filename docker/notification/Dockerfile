FROM openjdk:8-alpine

MAINTAINER yvlasiuk

ADD ./notification-0.0.1.jar /gls/notification/

WORKDIR /gls/notification

CMD java -jar -d64 -server -XX:+UseG1GC -Xms500M -Xmx1536M -XX:MaxMetaspaceSize=1536M -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
./notification-0.0.1.jar