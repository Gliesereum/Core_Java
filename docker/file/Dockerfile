FROM adoptopenjdk/openjdk11:alpine-jre

MAINTAINER yvlasiuk

ADD ./file-0.0.1.jar /gls/file/

WORKDIR /gls/file

CMD sleep 2m && java -jar -server -XX:+UseG1GC -Xms300M -Xmx1G -XX:MaxMetaspaceSize=1G -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
./file-0.0.1.jar