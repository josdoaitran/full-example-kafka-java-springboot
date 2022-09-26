# Example: Spring Boot with Kafka Consumer Example

How to use Spring Boot with Spring Kafka to Consume JSON/String message from Kafka topics.
## Setup local infra:
Setup infra: https://github.com/josdoaitran/setup-local-infra-testing4everyone/blob/main/local-infra-mysql-kafka.yml
## Run this command to setup Infra:
```
docker compose -f local-infra-mysql-kafka.yml up -d
```
## Create Database `fraud-check-db` via phpmyadmin

## Access to Kafka container
```aidl
> docker ps -a
CONTAINER ID   IMAGE                        COMMAND                  CREATED          STATUS          PORTS                                            NAMES
5ccff8d382d5   phpmyadmin/phpmyadmin        "/docker-entrypoint.…"   45 minutes ago   Up 45 minutes   0.0.0.0:9090->80/tcp                             phpmyadmin
a9615db3c2f9   johnnypark/kafka-zookeeper   "supervisord -n"         45 minutes ago   Up 45 minutes   0.0.0.0:2181->2181/tcp, 0.0.0.0:9092->9092/tcp   kafka
365372e938c5   mysql:5.7                    "sh -c ' /usr/local/…"   45 minutes ago   Up 45 minutes   0.0.0.0:3306->3306/tcp, 33060/tcp                mysql-5.7
 ~/Documents/personal-software-development -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- 11:12:46
> docker exec -it kafka bash
bash-4.4# cd /opt/
kafka_2.13-2.6.0/ kafka/
bash-4.4# cd /opt/
kafka_2.13-2.6.0/ kafka/
bash-4.4# cd /opt/kafka/
```

## Create Kafka `example Topics`
- `bin/kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic Kafka_Example`
- `bin/kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic Kafka_Example_json`

## Publish to the Kafka Topic via Console
- `bin/kafka-console-producer.sh --broker-list localhost:9092 --topic Kafka_Example`
```aidl
test message
```
- `bin/kafka-console-producer.sh --broker-list localhost:9092 --topic Kafka_Example_json`
```aidl
{"id":"9","name": "Example User", "phone": "0906973152", "status":"OPEN"}
```

## Listen Kafka via Console:

```aidl
bin/kafka-console-consumer.sh --topic Kafka_Example_json --from-beginning --bootstrap-server localhost:9092
```

# FraudService:


## Run the command to create Topic `UPDATE_USER_INFO_TOPIC`
```aidl
root@f0265d231c84:/opt/kafka# bin/kafka-topics.sh --create --topic UPDATE_USER_INFO_TOPIC --zookeeper zookeeper:2181 --partitions 1 --replication-factor 1
WARNING: Due to limitations in metric names, topics with a period ('.') or underscore ('_') could collide. To avoid issues it is best to use either, but not both.
Created topic UPDATE_USER_INFO_TOPIC.
```