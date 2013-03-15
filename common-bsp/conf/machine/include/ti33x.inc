SOC_FAMILY = "ti33x"
require conf/machine/include/soc-family.inc

require conf/machine/include/tune-cortexa8.inc
# Increase this everytime you change something in the kernel
MACHINE_KERNEL_PR = "r19"

KERNEL_IMAGETYPE = "uImage"

UBOOT_ENTRYPOINT = "0x80008000"
UBOOT_LOADADDRESS = "0x80008000"

EXTRA_IMAGEDEPENDS += "u-boot-denx"
