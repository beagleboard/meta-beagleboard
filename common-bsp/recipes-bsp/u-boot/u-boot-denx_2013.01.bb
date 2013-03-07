require u-boot.inc

# SPL build
UBOOT_BINARY = "u-boot.img"
UBOOT_IMAGE = "u-boot-${MACHINE}-${PV}-${PR}.img"
UBOOT_SYMLINK = "u-boot-${MACHINE}.img"

PV = "2013.01"

# No patches for other machines yet
COMPATIBLE_MACHINE = "(beaglebone)"

# File is board-specific, only copy when it will be correct.
FWENV = ""

SRC_URI = "git://www.denx.de/git/u-boot.git;protocol=git \
           file://0001-am335x-evm-enable-ext4.patch \
           file://0002-am335x-evm-add-support-for-BeagleBone-Black-DT-name.patch \
           file://0003-am335x-evm-switch-to-DT-boot.patch \
           file://0004-mmc-Add-an-mmcsilent-option.patch \
           file://0005-am335x_evm-Define-CONFIG_SYS_CACHELINE_SIZE.patch \
           file://0006-am335x_evm-Add-DFU-config.patch \
           file://0007-usb-Fix-bug-when-both-DFU-ETHER-are-defined.patch \
           file://0008-dfu-Only-perform-DFU-board_usb_init-for-TRATS.patch \
           file://0009-dfu-Fix-crash-when-wrong-number-of-arguments-given.patch \
           file://0010-dfu-Send-correct-DFU-response-from-composite_setup.patch \
           file://0011-dfu-Properly-zero-out-timeout-value.patch \
           file://0012-dfu-Add-a-partition-type-target.patch \
           file://0013-dfu-Support-larger-than-memory-transfers.patch \
           file://0014-Implement-nand_extent_skip_bad.patch \
           file://0015-Implement-NAND-dfu-support.patch \
           ${FWENV} \
          "


# v2013.01 tag + fixes
SRCREV = "9c748e02d99476e6a08d55eadfd8776edffe1e2e"

LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb"

S = "${WORKDIR}/git"

