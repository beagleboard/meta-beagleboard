#!/bin/bash

# (c) Copyright 2013 Koen Kooi <koen@dominion.thruhere.net>
# Licensed under terms of GPLv2

export PATH=$PATH:/bin:/sbin:/usr/bin:/usr/sbin

PART1MOUNT="/media/1"
PART2MOUNT="/media/2"

HOSTARCH="$(uname -m)"

cd /build

HEADER=$(hexdump -e '8/1 "%c"' /sys/bus/i2c/devices/0-0050/eeprom -s 5 -n 3)

if [ ${HEADER} -eq 335 ] ; then
        echo "Valid EEPROM header found"
else
        echo "Invalid EEPROM header detected"
	if [ -e /eeprom.dump ] ; then
		echo "Adding header to EEPROM"
		dd if=/eeprom.dump of=/sys/devices/ocp.2/44e0b000.i2c/i2c-0/0-0050/eeprom
	fi
fi


echo "Paritioning eMMC"
dd if=/dev/zero of=/dev/mmcblk1 bs=16M count=16
./mkcard.sh /dev/mmcblk1

echo "Mounting partitions"
mkdir -p ${PART1MOUNT}
mkdir -p ${PART2MOUNT}
mount /dev/mmcblk1p1 ${PART1MOUNT} -o relatime
mount /dev/mmcblk1p2 ${PART2MOUNT} -o async,noatime

echo "Copying bootloader files"
cp MLO u-boot.img ${PART1MOUNT}
echo "optargs=quiet drm.debug=7" >> ${PART1MOUNT}/uEnv.txt

sync

echo "Extracting rootfs"
tar zxf Angstrom-Cloud9-IDE-GNOME-eglibc-ipk-v2012.12-beaglebone.rootfs.tar.gz -C ${PART2MOUNT}

echo "Populating VFAT partition"
if [ -d ${PART2MOUNT}/usr/share/beaglebone-getting-started ] ; then
	cp -r ${PART2MOUNT}/usr/share/beaglebone-getting-started/* ${PART1MOUNT}
fi

echo "Cloud9 GNOME Image DATE" > ${PART1MOUNT}/ID.txt
echo "Cloud9 GNOME Image DATE" > ${PART2MOUNT}/etc/dogtag

umount ${PART1MOUNT}

if [ "${HOSTARCH}" = "armv7l" ] ; then

	echo "Generating machine ID"
	systemd-nspawn -D ${PART2MOUNT} /bin/systemd-machine-id-setup

	echo "Running Postinsts"
	cpufreq-set -g performance
	systemd-nspawn -D ${PART2MOUNT} /usr/bin/opkg-cl configure
	cpufreq-set -g ondemand

	# Hack to get some space back
	systemd-nspawn -D ${PART2MOUNT} /usr/bin/opkg-cl remove db-doc --force-depends

	#echo "Setting timezone to Europe/Paris"
	#systemd-nspawn -D ${PART2MOUNT} /usr/bin/timedatectl set-timezone Europe/Paris

fi

rm -f ${PART2MOUNT}/etc/pam.d/gdm-autologin

rm -f ${PART2MOUNT}/etc/systemd/system/multi-user.target.wants/xinput-calibrator.service
rm -f ${PART2MOUNT}/etc/systemd/system/multi-user.target.wants/busybox*
rm -f ${PART2MOUNT}/etc/dropbear/dropbear_rsa_host_key
ln -s /dev/null ${PART2MOUNT}/etc/systemd/system/xinetd.service

touch ${PART2MOUNT}/etc/default/locale

# enable wifi
mkdir -p ${PART2MOUNT}/var/lib/connman/
cp connman.settings ${PART2MOUNT}/var/lib/connman/settings

rm -f ${PART2MOUNT}/etc/network/interfaces

# Replace wallpaper
if [ -e ${PART2MOUNT}/usr/share/pixmaps/backgrounds/gnome/angstrom-default.jpg ] ; then
	cp beaglebg.jpg ${PART2MOUNT}/usr/share/pixmaps/backgrounds/gnome/angstrom-default.jpg
fi

umount ${PART2MOUNT}

sync

# verification stage

mount /dev/mmcblk1p1 ${PART1MOUNT} -o relatime

if [ -e ${PART1MOUNT}/ID.txt ] ; then
	echo "ID.txt found"
else
	echo "ID.txt missing - ERROR"
	ERROR="ID.txt"
fi

if [ -e ${PART1MOUNT}/START.htm ] ; then
	echo "START.htm found"
else
	echo "START.htm missing - ERROR"
	ERROR="${ERROR}, START.htm"
fi

umount ${PART1MOUNT}

mount /dev/mmcblk1p2 ${PART2MOUNT} -o async,noatime

BGMD5SUM_VALID="49b620077b1b78e198554e2272a5e172"
BGMD5SUM="$(md5sum ${PART2MOUNT}/usr/share/pixmaps/backgrounds/gnome/angstrom-default.jpg | awk '{print $1}')"

if [ "${BGMD5SUM_VALID}" != "${BGMD5SUM}" ] ; then
	echo "Wallpaper MD5sum failed"
	ERROR="${ERROR}, bgmd5"
fi

umount ${PART2MOUNT}

if [ -z "$ERROR" ] ; then
	if [ -e /sys/class/leds/beaglebone\:green\:usr0/trigger ] ; then
		echo default-on > /sys/class/leds/beaglebone\:green\:usr0/trigger
		echo default-on > /sys/class/leds/beaglebone\:green\:usr1/trigger
		echo default-on > /sys/class/leds/beaglebone\:green\:usr2/trigger
		echo default-on > /sys/class/leds/beaglebone\:green\:usr3/trigger
	fi
else
	echo "ERRORS found: ${ERROR}"
	if [ -e /sys/class/leds/beaglebone\:green\:usr0/trigger ] ; then
		echo none > /sys/class/leds/beaglebone\:green\:usr0/trigger
		echo none > /sys/class/leds/beaglebone\:green\:usr1/trigger
		echo none > /sys/class/leds/beaglebone\:green\:usr2/trigger
		echo none > /sys/class/leds/beaglebone\:green\:usr3/trigger
	fi
fi
