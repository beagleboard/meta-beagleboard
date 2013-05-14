DESCRIPTION = "Units to initialize usb gadgets"

inherit systemd

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58"

COMPATIBLE_MACHINE = "(beaglebone)"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://storage-gadget-init.service \
           file://udhcpd.service \
           file://udhcpd.conf \
           file://udhcpd.rules \
           file://g-storage-reinsert.sh \
           file://g-ether-start-service.sh \
           file://g-ether-load.sh \
          "

do_install() {
	install -d ${D}${base_libdir}/systemd/system
	install -m 0644 ${WORKDIR}/*.service ${D}${base_libdir}/systemd/system

	install -d ${D}${sysconfdir}/udev/rules.d
	install -m 0644 ${WORKDIR}/*.rules ${D}${sysconfdir}/udev/rules.d
	install -m 0644 ${WORKDIR}/*.conf ${D}${sysconfdir}

	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/*.sh ${D}${bindir}

	install -d ${D}${sysconfdir}/systemd/system/getty.target.wants
	( cd ${D}${sysconfdir}/systemd/system/getty.target.wants && ln -s /lib/systemd/system/serial-getty@.service serial-getty@ttyGS0.service )
}

PACKAGES =+ "${PN}-network ${PN}-udhcpd"

ALLOW_EMPTY_${PN} = "1"

FILES_${PN} = "${base_libdir}/systemd/system/storage-gadget-init.service \
               ${bindir}/g-storage-reinsert.sh \
               ${sysconfdir}/systemd/ \
              "

FILES_${PN}-network = "${base_libdir}/systemd/system/network-gadget-init.service \
                       ${bindir}/g-ether-load.sh \
                       ${bindir}/g-ether-start-service.sh \
                       ${sysconfdir}/udev/rules.d/udhcpd.rules"

FILES_${PN}-udhcpd = "${base_libdir}/systemd/system/udhcpd.service \
                      ${sysconfdir}/udhcpd.conf"

RRECOMMENDS_${PN} = "${PN}-network ${PN}-udhcpd"
RREPLACES_${PN} = "${PN}-storage"


NATIVE_SYSTEMD_SUPPORT = "1"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "storage-gadget-init.service"
