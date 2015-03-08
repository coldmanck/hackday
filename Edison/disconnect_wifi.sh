#!/bin/sh
# Script disconnect wifi
wpa_cli -i wlan0 disable_network 0 &>/dev/null
wpa_cli -i wlan0 remove_network 0 &>/dev/null
wpa_cli -i wlan0 disconnect &>/dev/null

echo "Disconnect"
