FROM adoptopenjdk/openjdk11:alpine-jre

MAINTAINER yvlasiuk

ADD ./mail-0.1.1.jar /gls/mail/

WORKDIR /gls/mail

CMD sleep 3m && java -jar -server -XX:+UseG1GC -Xms300M -Xmx1G -XX:MaxMetaspaceSize=1G -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
./mail-0.1.1.jar
