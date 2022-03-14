#!/usr/bin/env bash


file=$1

while read -r line
do
  echo "$line" |  kafka-console-producer.sh --broker-list localhost:9093 --topic $2
done < $file
