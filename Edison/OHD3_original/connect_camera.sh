#!/bin/sh
wpa_cli -i wlan0 remove_network 0 &>/dev/null
wpa_cli -i wlan0 add_network 0 
wpa_cli -i wlan0 set_network 0 ssid '"THETAXN10100001"'
wpa_cli -i wlan0 set_network 0 key_mgmt WPA-PSK
wpa_cli -i wlan0 set_network 0 proto WPA2
wpa_cli -i wlan0 set_network 0 pairwise CCMP
wpa_cli -i wlan0 set_network 0 group CCMP
wpa_cli -i wlan0 set_network 0 psk '"10100001"'
wpa_cli -i wlan0 enable_network 0 
wpa_cli -i wlan0 select_network 0

echo "Connect Camera Wifi success"
