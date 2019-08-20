FROM adoptopenjdk/openjdk11:alpine-jre

MAINTAINER yvlasiuk

ADD ./language-0.1.0.jar /gls/language/

WORKDIR /gls/language

CMD sleep 6m && java -jar -server -XX:+UseG1GC -Xms150M -Xmx1G -XX:MaxMetaspaceSize=1G -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
./language-0.1.0.jar
