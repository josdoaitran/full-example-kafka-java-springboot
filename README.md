# Full-example-kafka-java-sprintboot
Full-example-kafka-java-sprintboot

## Setup kafka local:
- Download kafka: https://kafka.apache.org/downloads
- Quick Start: https://kafka.apache.org/quickstart

+ Start Zookeper: 
```
bin/zookeeper-server-start.sh config/zookeeper.properties
```

+ Start Kafka Local:
```
bin/kafka-server-start.sh config/server.properties
```
+ Create topic *PUBLISH_NAME_TOPIC*
```
bin/kafka-topics.sh --create --topic CREATE_NEW_USER_TOPIC --bootstrap-server localhost:9092
```

+ Consume messae on topic *PUBLISH_NAME_TOPIC*
```
❯ bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic PUBLISH_NAME_TOPIC --from-beginning
{"name":"doai","dept":"Technology","salary":12000}
{"name":"testing4everyone","dept":"Technology","salary":12000}
```

## Setup Docker Kafka:

### Setup local infra Using Docker:
Setup infra: https://github.com/josdoaitran/setup-local-infra-testing4everyone/blob/main/local-infra-mysql-kafka.yml
### Run this command to setup Infra:
```
docker compose -f local-infra-mysql-kafka.yml up -d
```



# References:

- https://github.com/confluentinc/cp-all-in-one/blob/7.2.1-post/cp-all-in-one-community/docker-compose.yml
- https://kafka.apache.org/
- https://spring.io/projects/spring-boot
- https://www.youtube.com/watch?v=NjHYWEV_E_o


