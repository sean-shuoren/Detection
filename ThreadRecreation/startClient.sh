#!/bin/bash

javac Client.java
ts=$(date +%s%N)

java Client 48105

tt=$((($(date +%s%N) - $ts)/1000000))
echo "Time taken: $tt milliseconds"
