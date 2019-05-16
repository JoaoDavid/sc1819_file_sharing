#!/bin/bash

LOCAL_IP="10.101.149.51"

#clean Chains
sudo /sbin/iptables -F

#loopback nao filtrado
sudo /sbin/iptables -A INPUT -i lo -j ACCEPT
sudo /sbin/iptables -A OUTPUT -o lo -j ACCEPT

#ligacao estabelecida continua sendo aceite
sudo /sbin/iptables -A INPUT -m state --state ESTABLISHED,RELATED -j ACCEPT
sudo /sbin/iptables -A OUTPUT -m state --state ESTABLISHED,RELATED -j ACCEPT


#DCs
sudo /sbin/iptables -A INPUT -s 10.121.52.14 -d $LOCAL_IP -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.121.52.14 -s $LOCAL_IP -j ACCEPT

sudo /sbin/iptables -A INPUT -s 10.121.52.15 -d $LOCAL_IP -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.121.52.15 -s $LOCAL_IP -j ACCEPT

sudo /sbin/iptables -A INPUT -s 10.121.52.16 -d $LOCAL_IP -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.121.52.16 -s $LOCAL_IP -j ACCEPT

#Storage
sudo /sbin/iptables -A INPUT -s 10.121.72.23 -d $LOCAL_IP -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.121.72.23 -s $LOCAL_IP -j ACCEPT

#Iate/Falua
sudo /sbin/iptables -A INPUT -s 10.101.85.6 -d $LOCAL_IP -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.101.85.6 -s $LOCAL_IP -j ACCEPT

sudo /sbin/iptables -A INPUT -s 10.101.85.138 -d $LOCAL_IP -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.101.85.138 -s $LOCAL_IP -j ACCEPT

#Luna
sudo /sbin/iptables -A INPUT -s 10.101.85.24 -d $LOCAL_IP -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.101.85.24 -s $LOCAL_IP -j ACCEPT

#Gateway
sudo /sbin/iptables -A INPUT -s 10.101.148.1 -d $LOCAL_IP -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.101.148.1 -s $LOCAL_IP -j ACCEPT

#Proxy
sudo /sbin/iptables -A INPUT -s 10.101.85.134 -d $LOCAL_IP -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.101.85.134 -s $LOCAL_IP -j ACCEPT

#Ping - gcc
sudo /sbin/iptables -A INPUT -s 10.101.151.5 -p icmp -d $LOCAL_IP -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.101.151.5 -p icmp -s $LOCAL_IP -j ACCEPT

#Mask 255.255.254.0

#Ping - para as maquinas locais max.2 por segundo
sudo /sbin/iptables -A INPUT -p icmp -m icmp --icmp-type 8 -s 10.101.148.0/23 -j ACCEPT
sudo /sbin/iptables -A OUTPUT -p icmp -m icmp --icmp-type 8 -d 10.101.148.0/23 -m limit --limit 2/s -j ACCEPT

#SSH - maquinas
sudo /sbin/iptables -A INPUT -s 10.101.148.0/23 -p tcp -d $LOCAL_IP --dport 22 -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.101.148.0/23 -p tcp -s $LOCAL_IP --dport 22 -j ACCEPT

