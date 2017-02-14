#!/bin/bash

if ! which docker; then
    echo '[ERROR] Docker isnt installed.\n';
    exit 1;
fi

docker build -f checkout-app/Dockerfile amqp-failover-consistency/spring-base:0.0.1

registry.gitlab.com/guide-apps/amqp-failover-consistency:spring-builds-jdk8
