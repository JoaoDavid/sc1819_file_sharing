#!/bin/bash

#DCs
sudo /sbin/iptables -A INPUT -s 10.121.52.14 -p tcp -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.121.52.14 -p tcp -j ACCEPT

sudo /sbin/iptables -A INPUT -s 10.121.52.15 -p tcp -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.121.52.15 -p tcp -j ACCEPT

sudo /sbin/iptables -A INPUT -s 10.121.52.16 -p tcp -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.121.52.16 -p tcp -j ACCEPT


#Storage
sudo /sbin/iptables -A INPUT -s 10.121.72.23 -p tcp -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.121.72.23 -p tcp -j ACCEPT

#Iate/Falua
sudo /sbin/iptables -A INPUT -s 10.101.85.6 -p tcp -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.101.85.6 -p tcp -j ACCEPT

sudo /sbin/iptables -A INPUT -s 10.101.85.138 -p tcp -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.101.85.138 -p tcp -j ACCEPT

#Luna
sudo /sbin/iptables -A INPUT -s 10.101.85.24 -p tcp -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.101.85.24 -p tcp -j ACCEPT

#Gateway
sudo /sbin/iptables -A INPUT -s 10.101.148.1 -p tcp -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.101.148.1 -p tcp -j ACCEPT

#Proxy
sudo /sbin/iptables -A INPUT -s 10.101.85.134 -p tcp -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.101.85.134 -p tcp -j ACCEPT

#Ping - gcc
sudo /sbin/iptables -A INPUT -s  10.101.151.5 -p icmp -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d  10.101.151.5 -p icmp -j ACCEPT

#loopback nao filtrado
sudo /sbin/iptables -A INPUT -i lo -j ACCEPT
sudo /sbin/iptables -A OUTPUT -o lo -j ACCEPT

#ligacao estabelecida continua sendo aceite
sudo /sbin/iptables -A INPUT -p tcp -m state --state NEW -j ACCEPT

sudo /sbin/iptables -A INPUT -m state --state ESTABLISHED,RELATED -j ACCEPT
sudo /sbin/iptables -A OUTPUT -m state --state ESTABLISHED,RELATED -j ACCEPT

#Mask 255.255.254.0

#SSH - maquinas Sala 1.3.12
sudo /sbin/iptables -A INPUT -s  10.101.148.0/23 -p tcp -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d  10.101.148.0/23 -p tcp -j ACCEPT

#Ping - para as maquinas locais max.2 por segundo
sudo iptables -A OUTPUT -p icmp -m icmp --icmp-type 8 -m limit --limit 2/second -j ACCEPT

sudo /sbin/iptables -P OUTPUT DROP
sudo /sbin/iptables -F OUTPUT
