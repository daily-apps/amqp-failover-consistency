#!/bin/bash

if ! which docker; then
    echo '[ERROR] Docker not installed.\n';
    exit 1;
fi

# Up all cluster of rabbits, postgres, mail server and apps
docker-compose -f docker-compose.base.yml -f docker-compose.yml build
