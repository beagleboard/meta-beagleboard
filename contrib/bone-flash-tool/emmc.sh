#!/bin/bash

# (c) Copyright 2013 Koen Kooi <koen@dominion.thruhere.net>
# Licensed under terms of GPLv2

export PATH=$PATH:/bin:/sbin:/usr/bin:/usr/sbin

PART1MOUNT="/media/1"
PART2MOUNT="/media/2"

HOSTARCH="$(uname -m)"

cd /build

if [ -e /eeprom.dump ] ; then
	echo "Adding header to EEPROM"
	dd if=/eeprom.dump of=/sys/devices/ocp.2/44e0b000.i2c/i2c-0/0-0050/eeprom
fi

echo "Paritioning eMMC"
./mkcard.sh /dev/mmcblk1

echo "Mounting partitions"
mkdir -p ${PART1MOUNT}
mkdir -p ${PART2MOUNT}
mount /dev/mmcblk1p1 ${PART1MOUNT} -o relatime
mount /dev/mmcblk1p2 ${PART2MOUNT} -o relatime

echo "Copying bootloader files"
cp MLO u-boot.img ${PART1MOUNT}
echo "optargs=quiet" >> ${PART1MOUNT}/uEnv.txt

sync

echo "Extracting rootfs"
tar zxf Angstrom-Cloud9-IDE-GNOME-eglibc-ipk-v2012.12-beaglebone.rootfs.tar.gz -C ${PART2MOUNT}

echo "Populating VFAT partition"
if [ -d ${PART2MOUNT}/usr/share/beaglebone-getting-started ] ; then
	cp -r ${PART2MOUNT}/usr/share/beaglebone-getting-started/* ${PART1MOUNT}
fi

umount ${PART1MOUNT}

sync

if [ "${HOSTARCH}" = "armv7l" ] ; then

	echo "Generating machine ID"
	systemd-nspawn -D ${PART2MOUNT} /bin/systemd-machine-id-setup

	echo "Running Postinsts"
	cpufreq-set -g performance
	systemd-nspawn -D ${PART2MOUNT} /usr/bin/opkg-cl configure
	cpufreq-set -g ondemand

	sync

	#echo "Setting timezone to Europe/Paris"
	#systemd-nspawn -D ${PART2MOUNT} /usr/bin/timedatectl set-timezone Europe/Paris

fi

rm ${PART2MOUNT}/etc/pam.d/gdm-autologin

sync

umount ${PART2MOUNT}

if [ -e /sys/class/leds/beaglebone\:green\:usr0/trigger ] ; then
	echo default-on > /sys/class/leds/beaglebone\:green\:usr0/trigger
	echo default-on > /sys/class/leds/beaglebone\:green\:usr1/trigger
	echo default-on > /sys/class/leds/beaglebone\:green\:usr2/trigger
	echo default-on > /sys/class/leds/beaglebone\:green\:usr3/trigger
fi

