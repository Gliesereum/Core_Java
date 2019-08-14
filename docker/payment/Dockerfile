FROM adoptopenjdk/openjdk11:alpine-jre

MAINTAINER yvlasiuk

ADD ./payment-0.0.1.jar /gls/payment/

WORKDIR /gls/payment

ARG DEBUG_PORT

EXPOSE ${DEBUG_PORT}

CMD sleep 4m && java -jar -server -XX:+UseG1GC -Xms500M -Xmx1536M -XX:MaxMetaspaceSize=1536M -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=${DEBUG_PORT},suspend=n \
./payment-0.0.1.jar