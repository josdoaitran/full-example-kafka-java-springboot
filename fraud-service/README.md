# Example: Spring Boot with Kafka Consumer Example

How to use Spring Boot with Spring Kafka to Consume JSON/String message from Kafka topics.
## Setup local infra:
Setup infra: https://github.com/josdoaitran/setup-local-infra-testing4everyone/blob/main/local-infra-mysql-kafka.yml
## Run this command to setup Infra:
```
docker compose -f local-infra-mysql-kafka.yml up -d
```
## Create Database `fraud-check-db`

## Create Kafka Topic
- `bin/kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic Kafka_Example`
- `bin/kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic Kafka_Example_json`

## Publish to the Kafka Topic via Console
- `bin/kafka-console-producer.sh --broker-list localhost:9092 --topic Kafka_Example`
- `bin/kafka-console-producer.sh --broker-list localhost:9092 --topic Kafka_Example_json`
```aidl
{"id":"9","name": "Example User", "phone": "0906973152", "status":"PENDING"}
```

## Listen Kafka via Console:

```aidl
bin/kafka-console-consumer.sh --topic Kafka_Example_json --from-beginning --bootstrap-server localhost:9092
```

# FraudService:

