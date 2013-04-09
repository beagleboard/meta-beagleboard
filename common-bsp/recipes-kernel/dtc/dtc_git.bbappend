# Update DTC to latest git and apply DT overlay patch

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRCREV = "27cdc1b16f86f970c3c049795d4e71ad531cca3d"

SRC_URI += "file://0001-dtc-Dynamic-symbols-fixup-support.patch"

