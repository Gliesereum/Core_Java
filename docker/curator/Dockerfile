FROM python:3.7.1-alpine3.8

ENV CURATOR_VERSION=5.5.4

RUN pip install elasticsearch-curator==${CURATOR_VERSION} && \
     rm -rf /var/cache/apk/*

ADD ./log/elk/curator /curator

USER root

WORKDIR /curator

RUN chmod +x ./entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
