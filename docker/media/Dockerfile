FROM openjdk:8-alpine

MAINTAINER yvlasiuk

ADD ./media-0.0.1.jar /gls/media/

WORKDIR /gls/media

ARG PORT

EXPOSE ${PORT}

ARG PROFILE
ARG PROFILE="docker"

ARG DB_HOST
ARG DB_PORT
ARG DB_USERNAME
ARG DB_PASSWORD
ARG RABBIT_HOST
ARG RABBIT_PORT
ARG RABBIT_USER
ARG RABBIT_PASSWORD
ARG CDN_ACCESS_KEY
ARG CDN_SECRET_KEY
ARG CDN_HOST
ARG CDN_REGION
ARG CDN_BUCKET
ARG CDN_FOLDER
ARG EUREKA_HOST
ARG EUREKA_PORT


CMD java -jar -d64 -server -XX:+UseG1GC -Xms300M -Xmx1G -XX:MaxMetaspaceSize=1G -XX:+UseStringDeduplication -XX:+OptimizeStringConcat \
-Dspring.profiles.active=${PROFILE} \
-Dserver.port=${PORT} \
-Deureka.host=${EUREKA_HOST} \
-Deureka.port=${EUREKA_PORT} \
-Ddb.host=${DB_HOST} \
-Ddb.port=${DB_PORT} \
-Ddb.username=${DB_USERNAME} \
-Ddb.password=${DB_PASSWORD} \
-Drabbit.host=${RABBIT_HOST} \
-Drabbit.port=${RABBIT_PORT} \
-Drabbit.user=${RABBIT_USER} \
-Drabbit.password=${RABBIT_PASSWORD} \
-Dcdn.accessKey=${CDN_ACCESS_KEY} \
-Dcdn.secretKey=${CDN_SECRET_KEY} \
-Dcdn.host=${CDN_HOST} \
-Dcdn.region=${CDN_REGION} \
-Dcdn.bucket=${CDN_BUCKET} \
-Dcdn.folder=${CDN_FOLDER} \
./media-0.0.1.jar