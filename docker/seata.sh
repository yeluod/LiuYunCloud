#!/bin/bash

CONTAINER_NAME='seata-server'
IMAGES_NAME='seataio/seata-server:1.5.2'

CONTAINER_ID=$(docker ps -a | grep "${CONTAINER_NAME}" | grep -v "grep" | awk '{print $1}')
if [ -z "${CONTAINER_ID}" ]; then
    echo "${CONTAINER_NAME} container is not running"
else
    echo "docker delete ${CONTAINER_NAME}"
    docker rm -f "${CONTAINER_NAME}"
fi

echo 'docker run seata-server ...'

docker images | grep seata-server

docker run -itd --name "${CONTAINER_NAME}" \
    -p 8091:8091 \
    -p 7091:7091 \
    -v /Users/wangdong/work/java/personal/LiuYunCloud/docker/seata/config/application.yml:/seata-server/resources/application.yml \
    -e SEATA_IP=www.matewd.com \
    -e SEATA_PORT=8091 \
    "${IMAGES_NAME}"

CONTAINER_ID=$(docker ps | grep "${CONTAINER_NAME}" | grep -v "grep" | awk '{print $1}')

echo "CONTAINER ID IS ${CONTAINER_ID}"

# docker inspect "${CONTAINER_NAME}"