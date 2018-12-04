#!/bin/sh
FROM docker.elastic.co/elasticsearch/elasticsearch:6.4.2

USER root

RUN bin/elasticsearch-plugin install --batch com.floragunn:search-guard-6:6.4.2-23.2 \
    && chmod +x \
        plugins/search-guard-6/tools/hash.sh \
        plugins/search-guard-6/tools/sgadmin.sh \
        bin/init_sg.sh \
    && chown -R elasticsearch:elasticsearch /usr/share/elasticsearch
