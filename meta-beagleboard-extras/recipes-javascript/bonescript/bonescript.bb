DESCRIPTION = "Scripting tools for the BeagleBoard and BeagleBone"

inherit systemd

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=659ee0c98db2664403c769d6b9ab50eb"

SRCREV = "ecd1835cf68f92fffd4879b139c3b3429501cc19"

SRC_URI = "git://github.com/beagleboard/bonescript.git;protocol=git"

S = "${WORKDIR}/git"

do_install() {
	install -d ${D}${libdir}/node_modules/bonescript
	cp -a ${S}/node_modules/bonescript/* ${D}${libdir}/node_modules/bonescript

	install -d ${D}${localstatedir}/lib/cloud9
	cp -a ${S}/*.js ${D}${localstatedir}/lib/cloud9
	cp -a ${S}/LICENSE ${D}${localstatedir}/lib/cloud9
	cp -a ${S}/README.md ${D}${localstatedir}/lib/cloud9

	install -d ${D}${base_libdir}/systemd/system
	install -m 0644 ${S}/systemd/bonescript-autorun.service ${D}${base_libdir}/systemd/system
	install -m 0644 ${S}/systemd/bonescript.service ${D}${base_libdir}/systemd/system
	install -m 0644 ${S}/systemd/bonescript.socket ${D}${base_libdir}/systemd/system

	install -d ${D}${sysconfdir}/default
	install -m 0755 ${S}/etc/default/node ${D}${sysconfdir}/default

	install -d ${D}${sysconfdir}/profile.d
	install -m 0755 ${S}/etc/profile.d/node.sh ${D}${sysconfdir}/profile.d
}

NATIVE_SYSTEMD_SUPPORT = "1"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "bonescript-autorun.service bonescript.service bonescript.socket"

FILES_${PN} += "${libdir}/node_modules/bonescript ${base_libdir}/systemd/system ${sysconfdir}/profile.d"
RDEPENDS_${PN} = "nodejs bone101"

FILES_${PN}-dbg += "${libdir}/node_modules/bonescript/build/Release/.debug"
