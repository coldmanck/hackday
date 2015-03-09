#!/bin/sh
wpa_cli -i wlan0 remove_network 0 &>/dev/null
wpa_cli -i wlan0 add_network 0 
wpa_cli -i wlan0 set_network 0 ssid '"NaGi-iPhone"' &>/dev/null
wpa_cli -i wlan0 set_network 0 key_mgmt WPA-PSK &>/dev/null
wpa_cli -i wlan0 set_network 0 proto WPA2 &>/dev/null
wpa_cli -i wlan0 set_network 0 pairwise CCMP &>/dev/null
wpa_cli -i wlan0 set_network 0 group CCMP &>/dev/null
wpa_cli -i wlan0 set_network 0 psk '"848668488486"' &>/dev/null
wpa_cli -i wlan0 enable_network 0 
wpa_cli -i wlan0 select_network 0 

echo "Connect to Wifi"
