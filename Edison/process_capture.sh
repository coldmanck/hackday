#!/bin/sh

echo "Start take photo!"

# /home/root/disconnect_wifi.sh

/home/root/connect_camera.sh

while ! ping -c1 192.168.1.1 &> /dev/null; do :; done

java -jar /home/root/capture.jar

echo "Take Pic!"

# /home/root/disconnect_wifi.sh

/home/root/connect_wifi.sh

while ! ping -c1 8.8.8.8 &> /dev/null; do :; done

echo "Upload!"
java -jar /home/root/picUpload.jar
echo "upload done"
