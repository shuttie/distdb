#!/bin/sh

java -Djava.net.preferIPv4Stack=true -jar /app/distkv-assembly-1.0.jar --master $MASTER --slaves $SLAVES