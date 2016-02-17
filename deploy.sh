#!/bin/sh

sbt assembly
cd docker
cp ../target/scala-2.11/distdb-assembly-1.0.jar .
docker build -t shutty/distdb .
docker-compose up