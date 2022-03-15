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




Firstly, clone the repo to your machine by running following command:

```bash
git clone https://github.com/devibhattaraii/RCT-Dev-Deployment.git
```

## Installation of packages
Install dependencies following this link. https://www.tecmint.com/install-apache-spark-on-ubuntu/
Exception: Spark: 3.2.1, Scala: 2.12.15


## Running the Project

In order start the project, we will need Kafka server. 
In one terminal, run docker-compose and shift to another terminal.
```bash
sudo docker-compose up
```

## starting the Spark Server
In another terminal, start the spark sever using following command:
```bash
# starts the spark server
make logs

```

## sending Logs
In another terminal, using following command, send the logs:

```bash
make send-logs
```
To visit the Kafdrop UI, visit [http://localhost:8000](http://localhost:8000)

After this, we can see the changes in total number of logs in Kafka via Kafdrop UI under topic: WEBLOGS.

![Kafdrop-UI](https://github.com/devibhattaraii/BigData-Project/blob/0798ff4d215cb61adf6b3598f800b38eb0961169/Kafdrop-UI.png)

Then, the messages we just pushed to Kafka is shoown as below:
![messages](https://github.com/devibhattaraii/BigData-Project/blob/0798ff4d215cb61adf6b3598f800b38eb0961169/viewMessages.png)

## Watching analytics
In another terminal after closing "make logs", using following command, we can view analytics:

```bash
make log-analytics
```
After this, we can see the log via analytics run on Hive table data via sql.

![analytics](https://github.com/devibhattaraii/BigData-Project/blob/48e76e13a6f9b4d3df8b562be180bc4fe72f5b78/analytic.png)

## Working Demo in Fedora 35(workstation)
This UI screenshot is taken in Fedora 35 (workstation).
