#!/bin/bash
mvn clean package -f ../demoapiservice/pom.xml
mvn clean package -f ../demovisitorservice/pom.xml
trap ctrl_c INT

function ctrl_c() {
   docker kill $demoapiservice
   docker kill $demovisitorservice
   exit 1
}

docker build -t demoapiservice:1.0 ../demoapiservice
docker build -t demovisitorservice:1.0 ../demovisitorservice
demovisitorservice=$(docker run -d demovisitorservice:1.0)
VISITOR_SERVICE=$(docker inspect $demovisitorservice | jq -r '.[0]["NetworkSettings"]["IPAddress"]')
demoapiservice=$(docker run -e VISITOR_SERVICE=$VISITOR_SERVICE:9922 -p 7777:8080 -d demoapiservice:1.0)
while true; do
    sleep 1
    echo -n "."
done
