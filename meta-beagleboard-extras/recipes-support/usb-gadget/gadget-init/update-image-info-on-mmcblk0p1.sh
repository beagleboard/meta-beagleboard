#!/bin/sh
mount /dev/mmcblk0p1 /mnt
echo "Image info snapshot" > /mnt/info.txt

echo >> /mnt/info.txt
echo "/etc/angstrom-version:" >> /mnt/info.txt
cat /etc/angstrom-version >> /mnt/info.txt

echo >> /mnt/info.txt
echo "/proc/cpuinfo:" >> /mnt/info.txt
cat /proc/cpuinfo >> /mnt/info.txt

echo >> /mnt/info.txt
echo "uname -a:" >> /mnt/info.txt
uname -a >> /mnt/info.txt

echo >> /mnt/info.txt
echo "/proc/cmdline:" >> /mnt/info.txt
cat /proc/cmdline >> /mnt/info.txt

echo >> /mnt/info.txt
echo "ifconfig:" >> /mnt/info.txt
ifconfig >> /mnt/info.txt

echo >> /mnt/info.txt
echo "/etc/angstrom-build-info:" >> /mnt/info.txt
cat /etc/angstrom-build-info >> /mnt/info.txt

echo >> /mnt/info.txt
echo "/etc/image-version-info:" >> /mnt/info.txt
cat /etc/image-version-info >> /mnt/info.txt

echo >> /mnt/info.txt
echo "opkg list-installed:" >> /mnt/info.txt
opkg list-installed >> /mnt/info.txt

umount /mnt
