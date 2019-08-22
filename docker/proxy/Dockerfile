FROM adoptopenjdk/openjdk11:alpine-jre

MAINTAINER yvlasiuk

ADD ./proxy-0.1.1.jar /gls/proxy/

WORKDIR /gls/proxy

CMD sleep 5m && java -jar -server -XX:+UseG1GC -Xms500M -Xmx1536M -XX:MaxMetaspaceSize=1536M -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
./proxy-0.1.1.jar
