FROM registry.gitlab.com/guide-apps/amqp-failover-consistency:spring-base-0.0.2

COPY . /app
RUN mvn clean package -Dtest=**.unit.**.*Test,**.integration.**.*Test clean package

CMD ['java', '-jar', 'target/reports-app.jar']
