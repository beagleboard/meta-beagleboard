#!/bin/sh
if [ -a /sys/devices/platform/omap/musb-ti81xx/musb-hdrc.0/gadget/lun0/file ]
then
	x=$(cat /sys/devices/platform/omap/musb-ti81xx/musb-hdrc.0/gadget/lun0/file)
	if [ -z "$x" ]
	then
		/bin/systemctl stop storage-gadget-init.service
		/bin/systemctl start network-gadget-init.service
	fi
fi
