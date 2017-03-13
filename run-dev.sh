#!/bin/bash

if ! which docker; then
    echo '[ERROR] Docker not installed.\n';
    exit 1;
fi

# Build images
docker-compose -f docker-compose.base.yml -f docker-compose.dev.yml build

# Up all cluster of rabbits, postgres, mail server and apps
docker-compose -f docker-compose.base.yml up -d
docker-compose -f docker-compose.base.yml logs &
sleep 20

# Config cluster mode for slave
docker-compose -f docker-compose.base.yml exec rabbitmq_slave /bin/bash -c "rabbitmqctl stop_app; rabbitmqctl join_cluster rabbit@rabbitmq_master; rabbitmqctl start_app"
docker-compose -f docker-compose.base.yml exec rabbitmq_master rabbitmqctl set_policy ha-all "^payment\-" '{"ha-mode":"all", "ha-sync-mode":"automatic"}'

# Enable plugin to migrate messagens between queues
docker-compose -f docker-compose.base.yml exec rabbitmq_master rabbitmq-plugins enable rabbitmq_shovel rabbitmq_shovel_management

if [[ $# -eq 0 ]] ; then
    docker-compose -f docker-compose.base.yml -f docker-compose.dev.yml up --no-recreate
else
    docker-compose -f docker-compose.base.yml -f docker-compose.dev.yml up --no-recreate $1
fi
