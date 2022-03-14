MODULE=WEBLOGS

package:
	mvn compile package

# Rules related to logs
logs:
	spark-submit --class com.weblogs.stream.Main --master local target/SparkStreaming-0.0.1-SNAPSHOT-jar-with-dependencies.jar

log-analytics:
	spark-submit --class com.weblogs.stream.Analytics --master local target/SparkStreaming-0.0.1-SNAPSHOT-jar-with-dependencies.jar

send-logs:
	scripts/sendlogs.sh log.txt WEBLOGS

watch-logs:
	kafka-console-consumer --bootstrap-server localhost:9093 --topic WEBLOGS --from-beginning

# Rules related to events
send-events:
	scripts/sendlogs.sh events.txt EVENTS

watch-events:
	kafka-console-consumer --bootstrap-server localhost:9093 --topic EVENTS --from-beginning

events:
	spark-submit --class com.events.stream.Main --master local target/SparkStreaming-0.0.1-SNAPSHOT-jar-with-dependencies.jar

event-analytics:
	spark-submit --class com.events.stream.Analytics --master local target/SparkStreaming-0.0.1-SNAPSHOT-jar-with-dependencies.jar