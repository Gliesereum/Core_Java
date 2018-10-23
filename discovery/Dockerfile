FROM gradle:4.10.2-jdk8-alpine

ADD . /discovery

WORKDIR /discovery

ARG PORT

EXPOSE ${PORT}

ARG PROFILE
ARG PROFILE="docker"
ARG HOST="localhost"
CMD gradle clean build
CMD java -Xmx200m -jar -Dspring.profiles.active=${PROFILE} -Dserver.port=${PORT} -Deureka.instance.hostname=${HOST} ./build/libs/discovery-0.0.1-SNAPSHOT.jar
