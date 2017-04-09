This project aims to validate AMQP protocol failover capability using a microservice architecture with Docker Compose, RabbitMQ, PostgreSQL and Springboot.

To validate ***failover capability***, we will simulate a **`Product Purchase`** (product checkout) flow at one e-commerce website. A purchase represents a click at `buy button` Front-End that call some APIs representing Checkout flow, Payments Rules, Delivery Strategies, Reports and Notifications about product status.

To simulate a purchase scenarios, this project contains 4 apps running Jetty webservers and RabbitMQ clients:
- **Checkout App**: simulate a *simple purchase* process and notify event oriented architecture about it
- **Delivery App**: *catch purchase event*, and throw delivery process at distribution center
- **Reports App**: *store info* about purchase to generate business reports
- **Notifications App**: sent email to users *about purchase status* (bought, dispatched, delivered)

***Requirements***
- Docker and Docker compose
- Java8 docker image
- PostegreSQL docker image
- RabbitMQ docker image

***Techs***
- Java8 and SpringBoot
- PostegreSQL for storage
- RabbitMQ for AMQP messaging

### 1. Run everything

Use [Docker Compose](https://docs.docker.com/engine/installation/) to run all apps and third dependencies. Compose file will up 4 apps, 1 PostgreSQL database and 1 cluster of Rabbitmq AMQP servers:

```bash
# Will run apps and third dependencies using Docker Compose
$ ./run-dev.sh
```

*See at the bottom of this page the Architecture Diagram about communications with all parts of system.*


### 2. Simulate failover scenarios

Validate if everything is up running:

```bash
for i in {1..4};
do
    curl -s "http://localhost:900$i/status"
done

# if it displays 4 lines with 'ok' text, it works
```

`Fine! Lets test some schenarios`:

1. All works fine:
2. RabbitMQ slave die:
3. RabbitMQ master die:
4. Nothing works:

### 3. Architecture Diagram:

![Architecture Diagram](https://gitlab.com/guide-apps/amqp-failover-consistency/raw/master/doc/architecture-diagram.png)

Some improvements, features or challenges to this project? Send me an [e-mail](email:gabrielmassote@gmail.com) or a comment.

### 4. Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

Approvers will use the [Contributing Guide]() to `Approve` or `Reject` all Pull Requests.

### 5. Backlog, Bugs and Feedback

For backlog, bugs, questions and discussions please use the [Issues]().
