#!/bin/bash
rm -f *.log
rm -f jstack.*

javac Server.java servers/*.java
javac Client.java

port=$RANDOM

java Server $port &> server.log &
PID=$!
# jconsole $PID &

sleep 10
echo $PID

java Client $port &> client.log &
sleep 1
# jstack $PID > threaddump.log
./jStackSeries.sh $PID 24 0.01
