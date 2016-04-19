#!/bin/sh

prev=`cat /etc/resolv.conf`
echo "domain distdb.docker_default.docker" > /etc/resolv.conf
echo "nameserver 172.18.0.1" >> /etc/resolv.conf
echo "nameserver 8.8.8.8" >> /etc/resolv.conf