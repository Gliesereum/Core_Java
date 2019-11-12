#!/bin/sh

sleep 4m && curator --config ${CONFIG_FILE} ${COMMAND}

echo "$CRON curator --config ${CONFIG_FILE} ${COMMAND}" >>/etc/crontabs/root

crond -f -d 8 -l 8
