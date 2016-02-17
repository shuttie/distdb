#!/bin/sh

for i in `cat /proc/net/dev|grep eth|sed s/://g|awk '{print $1}'`; do ethtool -K $i tx off tso off gso off gro off; echo done $i; done