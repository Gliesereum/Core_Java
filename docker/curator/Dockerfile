FROM python:3.6.9-alpine3.10

ENV CURATOR_VERSION=5.7.6

RUN pip install elasticsearch-curator==${CURATOR_VERSION} && \
     rm -rf /var/cache/apk/*

ADD ./curator /curator

USER root

WORKDIR /curator

RUN chmod +x ./entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
