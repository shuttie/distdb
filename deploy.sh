#!/bin/sh

sbt assembly
cd docker
cp ../target/scala-2.11/distkv-assembly-1.0.jar .
docker build -t shutty/distkv .
docker-compose up