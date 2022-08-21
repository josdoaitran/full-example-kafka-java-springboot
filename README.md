# full-example-kafka-java-sprintboot
full-example-kafka-java-sprintboot

## Setup:
- Download kafka: https://kafka.apache.org/downloads
- https://kafka.apache.org/quickstart
- Setup Docker Kafka:

+ Check Docker container: 
```
docker-compose -f docker-kafka-compose.yml ps
 Name             Command            State                      Ports                   
----------------------------------------------------------------------------------------
kafka1   /etc/confluent/docker/run   Exit 1                                             
zoo1     /etc/confluent/docker/run   Up       0.0.0.0:2181->2181/tcp, 2888/tcp, 3888/tcp
```

+ Create a topic `pagev`:
```
docker-compose -f docker-kafka-compose.yml exec broker kafka-topics --create --bootstrap-server \\nlocalhost:9092 --replication-factor 1 --partitions 1 --topic pagev
```

# References:

- https://kafka.apache.org/
- https://spring.io/projects/spring-boot


