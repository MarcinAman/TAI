#!/usr/bin/env bash

echo ">Removing previous build files" &&
rm -rf ./dist/ &&
echo ">Building frontend" &&
ng build --prod &&
echo ">Adding rights to binary" &&
chmod -R o+rx ./dist/ &&
echo ">Building docker image" &&
docker build -t tai-frontend:1 . &&
echo "Build successful. New image name: tai-frontend:1"
