#!/usr/bin/env bash

echo ">Removing previous build" &&
rm -rf ./target/latest &&
echo ">Building project with sbt" &&
sbt dist &&
echo ">Unpacking" &&
unzip -d target/latest target/universal/*-1.0-SNAPSHOT.zip &&
mv target/latest/*/* target/latest/ &&
echo ">Removing windows script" &&
rm target/latest/bin/*.bat &&
echo ">Building image: tai:1" &&
docker build -t tai:1 . &&
echo "Build successful. New image name: tai:1"

#docker build -t tai:version .
#docker run -it -p 9000:9000 -p 9443:9443 --rm


