# Setup:

Setup infra: https://github.com/josdoaitran/setup-local-infra-testing4everyone/blob/main/local-infra-mysql-kafka.yml
## Run this command to setup Infra: 
```
docker compose -f local-infra-mysql-kafka.yml up -d
```

## Create Database `signup-user-db`
## Create Kafka topic:
- Access to Kafka container
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
- Run the command to create Topic `CREATE_NEW_USER_TOPIC`
```aidl
root@f0265d231c84:/opt/kafka# bin/kafka-topics.sh --create --topic CREATE_NEW_USER_TOPIC --zookeeper zookeeper:2181 --partitions 1 --replication-factor 1
WARNING: Due to limitations in metric names, topics with a period ('.') or underscore ('_') could collide. To avoid issues it is best to use either, but not both.
Created topic CREATE_NEW_USER_TOPIC.
```

- Run the command to list all topics: 
```aidl
root@f0265d231c84:/opt/kafka# bin/kafka-topics.sh --list --zookeeper zookeeper:2181
CREATE_NEW_USER_TOPIC
```

## Tests - User Service
- Run this command to trigger kafka
```
curl --location --request POST 'http://localhost:8081/kafka/publish/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name":"Testing4Everyone-new",
    "address":"HCM VietNam"    
}'
```

- Run command to listen/consume Kafka Topic:
```aidl
root@f0265d231c84:/opt/kafka# kafka-console-consumer.sh --topic CREATE_NEW_USER_TOPIC --from-beginning --bootstrap-server localhost:9092
{"id":0,"name":"Testing4Everyone-new","address":"HCM VietNam"}
```

