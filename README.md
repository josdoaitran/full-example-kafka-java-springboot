# full-example-kafka-java-sprintboot
full-example-kafka-java-sprintboot

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

- Setup Docker Kafka:

+ Run Docker file:
```
docker-compose up -d
```
+ Check Docker container: 
```
docker-compose ps
 Name             Command            State                      Ports                   
----------------------------------------------------------------------------------------
kafka1   /etc/confluent/docker/run   Exit 1                                             
zoo1     /etc/confluent/docker/run   Up       0.0.0.0:2181->2181/tcp, 2888/tcp, 3888/tcp
```

+ Create a topic pagev:

```
docker-compose -f docker-kafka-compose.yml exec broker kafka-topics --create --bootstrap-server \\nlocalhost:9092 --replication-factor 1 --partitions 1 --topic pagev
```

# References:

- https://github.com/confluentinc/cp-all-in-one/blob/7.2.1-post/cp-all-in-one-community/docker-compose.yml
- https://kafka.apache.org/
- https://spring.io/projects/spring-boot
- https://www.youtube.com/watch?v=NjHYWEV_E_o


