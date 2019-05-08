#!/usr/bin/env bash

rm -rf ./target/latest &&
sbt dist &&
unzip -d target/latest target/universal/*-1.0-SNAPSHOT.zip &&
mv target/latest/*/* target/latest/
rm target/latest/bin/*.bat

#docker build -t tai:version .
#docker run -it -p 9000:9000 -p 9443:9443 --rm


