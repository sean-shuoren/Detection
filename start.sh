#!/bin/bash
rm -f *.log
javac Server.java
javac Client.java

port=$RANDOM

java Server $port &> Server.log &
PID=$!
sleep 1
echo $PID

java Client $port &> client.log &
sleep 1
jstack $PID > threaddump.log
