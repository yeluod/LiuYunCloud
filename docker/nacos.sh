#!/bin/bash

CONTAINER_NAME='nacos-server'
IMAGES_NAME='nacos/nacos-server:v2.2.0'
LOCAL_MOUNT_VOLUME='/Users/wangdong/docker/nacos'
NACOS_APPLICATION_PORT='8848'
MYSQL_SERVICE_HOST='59.110.238.123'
MYSQL_SERVICE_PORT='3306'
MYSQL_SERVICE_DB_NAME='liuyun-nacos'
MYSQL_SERVICE_USER='root'
MYSQL_SERVICE_PASSWORD='Yeluo@3306'
MYSQL_SERVICE_DB_PARAM='characterEncoding=utf8&connectTimeout=10000&socketTimeout=30000&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai'

CONTAINER_ID=$(docker ps | grep "${CONTAINER_NAME}" | grep -v "grep" | awk '{print $1}')
if [ -z "${CONTAINER_ID}" ]; then
    echo "${CONTAINER_NAME} container is not running"
else
    echo "docker delete ${CONTAINER_NAME}"
    docker rm -f "${CONTAINER_NAME}"
fi

echo 'docker run nacos ...'

docker images | grep "${IMAGES_NAME}"

docker run -itd --name nacos-server \
    -p 8848:8848 \
    -p 9848:9848 \
    -v "${LOCAL_MOUNT_VOLUME}"/data:/home/nacos/data \
    -v "${LOCAL_MOUNT_VOLUME}"/logs:/home/nacos/logs \
    -e MODE=standalone \
    -e PREFER_HOST_MODE=ip \
    -e NACOS_APPLICATION_PORT="${NACOS_APPLICATION_PORT}" \
    -e SPRING_DATASOURCE_PLATFORM=mysql \
    -e MYSQL_SERVICE_HOST="${MYSQL_SERVICE_HOST}" \
    -e MYSQL_SERVICE_PORT="${MYSQL_SERVICE_PORT}" \
    -e MYSQL_SERVICE_DB_NAME="${MYSQL_SERVICE_DB_NAME}" \
    -e MYSQL_SERVICE_USER="${MYSQL_SERVICE_USER}" \
    -e MYSQL_SERVICE_PASSWORD="${MYSQL_SERVICE_PASSWORD}" \
    -e MYSQL_SERVICE_DB_PARAM="${MYSQL_SERVICE_DB_PARAM}" \
    -e JVM_XMX=256m \
    -e JVM_XMS=256m \
    -e JVM_XMN=256m \
    "${IMAGES_NAME}"
