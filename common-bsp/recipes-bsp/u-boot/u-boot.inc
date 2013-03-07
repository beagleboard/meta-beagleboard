DESCRIPTION = "U-Boot - the Universal Boot Loader"
HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=4c6cde5df68eff615d36789dc18edd3b"

PROVIDES = "virtual/bootloader"

DEPENDS = "mtd-utils"

PACKAGE_ARCH = "${MACHINE_ARCH}"
PARALLEL_MAKE=""

EXTRA_OEMAKE = "CROSS_COMPILE=${TARGET_PREFIX}"

UBOOT_MACHINE ?= "${MACHINE}_config"
UBOOT_BINARY ?= "u-boot.bin"
UBOOT_IMAGE ?= "u-boot-${MACHINE}-${PV}-${PR}.bin"
UBOOT_SYMLINK ?= "u-boot-${MACHINE}.bin"
UBOOT_MAKE_TARGET ?= "all"

do_configure () {
	oe_runmake ${UBOOT_MACHINE}
}

do_compile () {
	unset LDFLAGS
	unset CFLAGS
	unset CPPFLAGS
	oe_runmake ${UBOOT_MAKE_TARGET}
}

do_install () {
	install -d ${D}/boot
	install ${S}/${UBOOT_BINARY} ${D}/boot/${UBOOT_IMAGE}
	ln -sf ${UBOOT_IMAGE} ${D}/boot/${UBOOT_BINARY}

	if [ -e ${WORKDIR}/fw_env.config ] ; then
		install -d ${D}${sysconfdir}
		install -m 644 ${WORKDIR}/fw_env.config ${D}${sysconfdir}/fw_env.config
	fi

}

FILES_${PN} = "/boot ${sysconfdir}"
# no gnu_hash in uboot.bin, by design, so skip QA
INSANE_SKIP_${PN} = "1"

inherit deploy

addtask deploy before do_package after do_compile

do_deploy () {
	install -d ${DEPLOY_DIR_IMAGE}
	install ${S}/${UBOOT_BINARY} ${DEPLOY_DIR_IMAGE}/${UBOOT_IMAGE}

	cd ${DEPLOY_DIR_IMAGE}
	rm -f ${UBOOT_BINARY} ${UBOOT_SYMLINK}
	ln -sf ${UBOOT_IMAGE} ${UBOOT_SYMLINK}
	ln -sf ${UBOOT_IMAGE} ${UBOOT_BINARY}

	if [ -e ${S}/MLO ] ; then
		install ${S}/MLO ${DEPLOY_DIR_IMAGE}/MLO-${MACHINE}-${PV}
		rm -f ${DEPLOY_DIR_IMAGE}/MLO-${MACHINE} ${DEPLOY_DIR_IMAGE}/MLO
		ln -sf MLO-${MACHINE}-${PV} ${DEPLOY_DIR_IMAGE}/MLO-${MACHINE}
		ln -sf MLO-${MACHINE}-${PV} ${DEPLOY_DIR_IMAGE}/MLO
	fi
}
