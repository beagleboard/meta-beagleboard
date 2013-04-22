DESCRIPTION = "BeagleBone Getting Started Guide"

inherit allarch

LICENSE = "GPLv2+ & MIT & PD & others"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=4040d560ed8da9b0873348b25b3f45b4"

SRCREV = "bee223b4cff3a67e7146136e6840d24a74a3c419"
SRC_URI = "git://github.com/beagleboard/beaglebone-getting-started.git"

S = "${WORKDIR}/git"

do_install() {
	install -d ${D}${datadir}/${PN}
	cp -a ${S}/* ${D}${datadir}/${PN}
}

FILES_${PN} += "${datadir}/${PN}"
