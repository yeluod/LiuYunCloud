#!/bin/bash

CONTAINER_NAME='redis-server'
IMAGES_NAME='redis:latest'
LOCAL_MOUNT_VOLUME='/Users/wangdong/docker/redis'
MYSQL_ROOT_PASSWORD='123456'

CONTAINER_ID=$(docker ps -a | grep "${CONTAINER_NAME}" | grep -v "grep" | awk '{print $1}')
if [ -z "${CONTAINER_ID}" ]; then
    echo "${CONTAINER_NAME} container is not running"
  else
    echo "docker delete ${CONTAINER_NAME}"
    docker rm -f "${CONTAINER_NAME}"
  fi

echo 'docker run mysql ...'

docker images | grep mysql

docker run -itd --name "${CONTAINER_NAME}" \
  -p 6379:6379 \
  -v "${LOCAL_MOUNT_VOLUME}"/data:/data \
  "${IMAGES_NAME}" \
  --appendonly yes \
  --requirepass "${MYSQL_ROOT_PASSWORD}"

CONTAINER_ID=$(docker ps | grep "${CONTAINER_NAME}" | grep -v "grep" | awk '{print $1}')

echo "CONTAINER ID IS ${CONTAINER_ID}"

# docker inspect "${CONTAINER_NAME}"