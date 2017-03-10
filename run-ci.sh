#!/bin/bash

if ! which docker; then
    echo '[ERROR] Docker not installed.\n';
    exit 1;
fi

docker-compose -f docker-compose.base.yml -f docker-compose.ci.yml down

# Build images
docker-compose -f docker-compose.base.yml -f docker-compose.ci.yml build

# After docker images builds, up base services
docker-compose -f docker-compose.base.yml up -d
docker-compose -f docker-compose.base.yml logs &
sleep 15

# Config cluster mode for slave
docker-compose -f docker-compose.base.yml exec rabbitmq_slave /bin/bash -c "rabbitmqctl stop_app; rabbitmqctl join_cluster rabbit@rabbitmq_master; rabbitmqctl start_app"
docker-compose -f docker-compose.base.yml exec rabbitmq_master rabbitmqctl set_policy ha-all "^payment\-" '{"ha-mode":"all", "ha-sync-mode":"automatic"}'

# Enable plugin to migrate messagens between queues
docker-compose -f docker-compose.base.yml exec rabbitmq_master rabbitmq-plugins enable rabbitmq_shovel rabbitmq_shovel_management

# Run acceptance tests after docker images builds, and env steup
# Better Practice: change all 4 acceptance tests for cucumber test, like:
#   docker-compose -f docker-compose.base.yml -f docker-compose.ci.yml run cucumber-acceptance-test
if [[ $# -eq 0 ]] ; then
    declare -a apps=("checkout_app" "gateway_app")

    for app in "${apps[@]}"
    do
        docker-compose -f docker-compose.base.yml -f docker-compose.ci.yml run $app
    done
else
    docker-compose -f docker-compose.base.yml -f docker-compose.ci.yml run --no-deps $1
fi

# docker-compose -f docker-compose.base.yml -f docker-compose.ci.yml down
