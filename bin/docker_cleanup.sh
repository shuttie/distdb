#!/bin/sh

echo "Cleaning up dead containers"
docker ps --filter status=dead --filter status=exited -aq | xargs docker rm -v

echo "Cleaning up non-tagged images"
docker images --no-trunc | grep '<none>' | awk '{ print $3 }' | xargs -r docker rmi