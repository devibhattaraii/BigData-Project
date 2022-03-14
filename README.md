# Spark Streaming integration with Kafka and Hive

This project created by using Apache Spark Discretized Stream (DStreams) to read web requests logs data as messages 
from Apache Kafka and store them inside Apache Hive table  
```
spark-submit --class com.weblogs.stream.WebLogStreaming --master local[2] target/SparkStreaming-0.0.1-SNAPSHOT.jar
```

## Create topic

```bash
# create a topic
kafka-topics --create --replication-factor 1 --bootstrap-server localhost:9095 --partitions 1 --topic weblog_out
kafka-topics --create --replication-factor 1 --bootstrap-server localhost:9095 --partitions 1 --topic WebLogs
# create events in consumer
kafka-console-consumer --bootstrap-server localhost:9095 --topic WebLogs --from-beginning
# producer
kafka-console-producer --broker-list localhost:9092 --topic WebLogs
```
## Sample of the data

10.131.2.1,30/Nov/2017:15:50:53,GET /details.php?id=43 HTTP/1.1,200

10.131.2.1,30/Nov/2017:15:34:56,POST /process.php HTTP/1.1,302

10.131.2.1,02/Dec/2017:18:35:48,GET /fonts/fontawesome-webfont.woff2?v=4.6.3 HTTP/1.1,304

10.129.2.1,14/Nov/2017:02:54:51,GET /robots.txt HTTP/1.1,404

10.130.2.1,22/Nov/2017:23:21:04,POST /process.php HTTP/1.1,302

### Data source

* https://www.kaggle.com/shawon10/web-log-dataset