#!/bin/bash
#!/bin/bash
function ctrl_c() {
   kill $visitorservicepid
   kill $apiservicepid
   exit 1
}
trap ctrl_c INT
mvn clean package -f ../demoapiservice/pom.xml
mvn clean package -f ../demovisitorservice/pom.xml
java -jar ../demovisitorservice/target/demovisitorservice-1.0-SNAPSHOT.jar &
visitorservicepid=$!
VISITOR_SERVICE=localhost:9922 java -jar ../demoapiservice/target/demoapiservice-1.0-SNAPSHOT.jar &
apiservicepid=$!
while true; do
    sleep 1
    echo -n "."
done
