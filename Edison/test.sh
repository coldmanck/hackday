#!/bin/sh


./disconnect_wifi.sh

./connect_camera.sh

while ! ping -c1 192.168.1.1 &> /dev/null; do :; done

java -jar ./capture.jar

./disconnect_wifi.sh

./connect_wifi.sh

while ! ping -c1 8.8.8.8 &> /dev/null; do :; done

java -jar ./picUpload.jar
echo "upload done"
