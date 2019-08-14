FROM adoptopenjdk/openjdk11:alpine-jre

MAINTAINER yvlasiuk

ADD ./lendinggallery-0.0.1.jar /gls/lending-gallery/

WORKDIR /gls/lending-gallery

CMD sleep 3m && java -jar -server -XX:+UseG1GC -Xms500M -Xmx1536M -XX:MaxMetaspaceSize=1536M -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
./lendinggallery-0.0.1.jar