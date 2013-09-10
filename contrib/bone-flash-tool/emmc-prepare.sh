#!/bin/sh

IMAGE=$1

DEPLOYDIR="/build/v2013.06/deploy/eglibc/images/beaglebone/"
MOUNTPOINT="/media/2"
MOUNTPOINT1="/media/1"
FLASHIMG="Angstrom-Cloud9-IDE-GNOME-eglibc-ipk-v2013.06-beaglebone.rootfs.tar.gz"
SCRATCHDIR="/build/images"

EMMCSCRIPT="/build/v2013.06/sources/meta-beagleboard/contrib/bone-flash-tool/emmc.sh"

DATE="$(ls -o  --time-style +' %Y.%m.%d' ${DEPLOYDIR}/${FLASHIMG} | awk '{print $5}')-DO-NOT-USE-FOR-PRODUCTION"
#DATE="$(date +'%Y.%m.%d')-DO-NOT-USE-FOR-PRODUCTION"

echo "Using ${DATE} as identifier"

IMAGENAME="$(basename ${IMAGE} .img)-${DATE}"

if ! [ -e ${IMAGENAME} ] ; then
	echo "uncompressing image"
	xz -k -d -v -c ${IMAGE}.xz > ${IMAGENAME}
fi

if ! [ -e ${IMAGENAME} ] ; then
	echo "${IMAGENAME} not found!"
	exit 1
fi

echo "Trying to attach image file"
LOOPFILE="$(kpartx -a -v ${IMAGENAME} | grep /dev | grep p2 | tail -n1 | awk '{print $8}' | sed s:/dev/::)"

echo "Loopdev: ${LOOPFILE}"

sleep 1

if ! [ -e /dev/mapper/${LOOPFILE}p1 ] ; then
	echo "Incorrect partitioning, /dev/mapper/${LOOPFILE}p1 not found"
	exit 1
fi

if grep -q "${MOUNTPOINT1}" /etc/mtab ; then
        echo "${MOUNTPOINT1} already mounted, trying to unmount"
        umount ${MOUNTPOINT1}
fi

echo "Mounting /dev/mapper/${LOOPFILE}p1"
mount /dev/mapper/${LOOPFILE}p1 ${MOUNTPOINT1} || exit 1

echo "BeagleBone Black eMMC flasher ${DATE}" > ${MOUNTPOINT1}/ID.txt

umount ${MOUNTPOINT1} || exit 1

if ! [ -e /dev/mapper/${LOOPFILE}p2 ] ; then
	echo "Incorrect partitioning, /dev/mapper/${LOOPFILE}p2 not found"
	exit 1
fi

umount ${MOUNTPOINT}

echo "Mounting /dev/mapper/${LOOPFILE}p2"
mount /dev/mapper/${LOOPFILE}p2 ${MOUNTPOINT} || exit 1

echo "Tarring up the contents"
( cd ${MOUNTPOINT} && rm -f build/${FLASHIMG} && tar cf ${SCRATCHDIR}/flash.tar . )

sleep 1

umount ${MOUNTPOINT} || exit 1

echo "Zeroing /dev/mapper/${LOOPFILE}p2"
dd if=/dev/zero of=/dev/mapper/${LOOPFILE}p2

echo "Creating ext4 on /dev/mapper/${LOOPFILE}p2"
mkfs.ext4 -L eMMC-Flasher /dev/mapper/${LOOPFILE}p2 || exit 1

echo "Mounting /dev/mapper/${LOOPFILE}p2 again"
mount /dev/mapper/${LOOPFILE}p2 ${MOUNTPOINT} || exit 1

echo "Untarring contents again"
tar xf ${SCRATCHDIR}/flash.tar -C ${MOUNTPOINT}

echo "Copying over bootloader and tarball"
cp -vf ${DEPLOYDIR}/MLO ${MOUNTPOINT}/build/ && cp -vf ${DEPLOYDIR}/u-boot.img ${MOUNTPOINT}/build/ && cp -vf ${DEPLOYDIR}/${FLASHIMG} ${MOUNTPOINT}/build/

echo "Copying over flashing script"
cp -vf ${EMMCSCRIPT} ${MOUNTPOINT}/usr/bin/ 

rm -f ${MOUNTPOINT}/build/emmc.sh

MLOMD5="$(md5sum ${DEPLOYDIR}/MLO | awk '{print $1}')"
UBOOTMD5="$(md5sum ${DEPLOYDIR}/u-boot.img | awk '{print $1}')"

sed -i -e s:DATE:${DATE}:g \
       -e s:MD5MLO:${MLOMD5}:g \
       -e s:MD5UBOOT:${UBOOTMD5}:g \
       ${MOUNTPOINT}/usr/bin/emmc.sh

sync && sleep 1

echo "Ummounting ${MOUNTPOINT}"
umount ${MOUNTPOINT}

sleep 1

echo "detaching loopfile"
kpartx -d -v ${IMAGENAME}

echo "Compressing image"
xz -f -v -z -T0 -e -9 ${IMAGENAME} 
cp -f ${IMAGE}.xz /build/dominion/beaglebone/${IMAGENAME}.xz
