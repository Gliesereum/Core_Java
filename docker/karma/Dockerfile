FROM adoptopenjdk/openjdk11:alpine-jre

MAINTAINER vitalij

ADD ./karma-0.1.1.jar /gls/karma/

WORKDIR /gls/karma

CMD sleep 5m && java -jar -server -XX:+UseG1GC -Xms500M -Xmx1536M -XX:MaxMetaspaceSize=1536M -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
./karma-0.1.1.jar