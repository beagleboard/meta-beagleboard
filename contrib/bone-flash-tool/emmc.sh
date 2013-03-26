#!/bin/bash

# (c) Copyright 2013 Koen Kooi <koen@dominion.thruhere.net>
# Licensed under terms of GPLv2

export PATH=$PATH:/bin:/sbin:/usr/bin:/usr/sbin

cd /build

if [ -e /eeprom.dump ] ; then
	echo "Adding header to EEPROM"
	dd if=/eeprom.dump of=/sys/devices/ocp.2/44e0b000.i2c/i2c-0/0-0050/eeprom
fi

echo "Paritioning eMMC"
./mkcard.sh /dev/mmcblk1

echo "Mounting partitions"
mkdir -p /media/1
mkdir -p /media/2
mount /dev/mmcblk1p1 /media/1 -o async
mount /dev/mmcblk1p2 /media/2 -o async,noatime

echo "Copying bootloader files"
cp MLO u-boot.img /media/1
echo "mmcdev=1" > /media/1/uEnv.txt
echo "optargs=quiet" >> /media/1/uEnv.txt

sync

echo "Extracting rootfs"
tar zxf Angstrom-Cloud9-IDE-GNOME-eglibc-ipk-v2012.12-beaglebone.rootfs.tar.gz -C /media/2

echo "Populating VFAT partition"
if [ -d /media/2/usr/share/beaglebone-getting-started ] ; then
	cp -r /media/2/usr/share/beaglebone-getting-started/* /media/1
fi

umount /media/1

echo "Generating machine ID"
systemd-nspawn -D /media/2 /bin/systemd-machine-id-setup

echo "Running Postinsts"
cpufreq-set -g performance
systemd-nspawn -D /media/2 /usr/bin/opkg-cl configure
cpufreq-set -g ondemand

#echo "Setting timezone to Europe/Paris"
#systemd-nspawn -D /media/2 /usr/bin/timedatectl set-timezone Europe/Paris

rm /media/2/etc/pam.d/gdm-autologin

umount /media/2

sync

echo default-on > /sys/class/leds/beaglebone\:green\:usr0/trigger
echo default-on > /sys/class/leds/beaglebone\:green\:usr1/trigger
echo default-on > /sys/class/leds/beaglebone\:green\:usr2/trigger
echo default-on > /sys/class/leds/beaglebone\:green\:usr3/trigger


