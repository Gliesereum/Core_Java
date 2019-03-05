#!/bin/sh

echo "$CRON curator --config ${CONFIG_FILE} ${COMMAND}" >>/etc/crontabs/root

crond -f -d 8 -l 8