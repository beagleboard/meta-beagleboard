require linux.inc

DESCRIPTION = "Linux kernel"
KERNEL_IMAGETYPE = "uImage"

COMPATIBLE_MACHINE = "(beaglebone)"

# The main PR is now using MACHINE_KERNEL_PR, for omap3 see conf/machine/include/omap3.inc
MACHINE_KERNEL_PR_append = "a"

FILESPATH =. "${FILE_DIRNAME}/linux-mainline-3.7:${FILE_DIRNAME}/linux-mainline-3.7/${MACHINE}:"

S = "${WORKDIR}/git"

PV = "3.7.0"


SRCREV_pn-${PN} = "9924a1992a86ebdb7ca36ef790d2ba0da506296c"

SRC_URI = " \
	git://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux.git;branch=master \
	file://dma/0001-video-st7735fb-add-st7735-framebuffer-driver.patch \
	file://dma/0002-regulator-tps65910-fix-BUG_ON-shown-with-vrtc-regula.patch \
	file://dma/0003-dmaengine-add-helper-function-to-request-a-slave-DMA.patch \
	file://dma/0004-of-Add-generic-device-tree-DMA-helpers.patch \
	file://dma/0005-of-dma-fix-build-break-for-CONFIG_OF.patch \
	file://dma/0006-of-dma-fix-typos-in-generic-dma-binding-definition.patch \
	file://dma/0007-dmaengine-fix-build-failure-due-to-missing-semi-colo.patch \
	file://dma/0008-dmaengine-edma-fix-slave-config-dependency-on-direct.patch \
	file://dma/0009-ARM-davinci-move-private-EDMA-API-to-arm-common.patch \
	file://dma/0010-ARM-edma-remove-unused-transfer-controller-handlers.patch \
	file://dma/0011-ARM-edma-add-DT-and-runtime-PM-support-for-AM33XX.patch \
	file://dma/0012-ARM-edma-add-AM33XX-crossbar-event-support.patch \
	file://dma/0013-dmaengine-edma-enable-build-for-AM33XX.patch \
	file://dma/0014-dmaengine-edma-Add-TI-EDMA-device-tree-binding.patch \
	file://dma/0015-ARM-dts-add-AM33XX-EDMA-support.patch \
	file://dma/0016-dmaengine-add-dma_request_slave_channel_compat.patch \
	file://dma/0017-mmc-omap_hsmmc-convert-to-dma_request_slave_channel_.patch \
	file://dma/0018-mmc-omap_hsmmc-limit-max_segs-with-the-EDMA-DMAC.patch \
	file://dma/0019-mmc-omap_hsmmc-add-generic-DMA-request-support-to-th.patch \
	file://dma/0020-ARM-dts-add-AM33XX-MMC-support.patch \
	file://dma/0021-spi-omap2-mcspi-convert-to-dma_request_slave_channel.patch \
	file://dma/0022-spi-omap2-mcspi-add-generic-DMA-request-support-to-t.patch \
	file://dma/0023-ARM-dts-add-AM33XX-SPI-support.patch \
	file://dma/0024-Documentation-bindings-add-spansion.patch \
	file://dma/0025-ARM-dts-add-BeagleBone-Adafruit-1.8-LCD-support.patch \
	file://dma/0026-misc-add-gpevt-driver.patch \
	file://dma/0027-ARM-dts-add-BeagleBone-gpevt-support.patch \
	file://dma/0028-ARM-configs-working-AM33XX-edma-dmaengine-defconfig.patch \
	file://dma/0029-ARM-configs-working-da850-edma-dmaengine-defconfig.patch \
	file://dma/0030-misc-gpevt-null-terminate-the-of_match_table.patch \
	file://dma/0031-proposed-probe-fix-works-for-me-on-evm.patch \
	file://cpsw/0001-ARM-OMAP3-hwmod-Add-AM33XX-HWMOD-data-for-davinci_md.patch \
	file://cpsw/0002-net-davinci_mdio-Fix-type-mistake-in-calling-runtime.patch \
	file://cpsw/0003-net-cpsw-Add-parent-child-relation-support-between-c.patch \
	file://cpsw/0004-arm-dts-am33xx-Add-cpsw-and-mdio-module-nodes-for-AM.patch \
	file://pinctrl/0001-i2c-pinctrl-ify-i2c-omap.c.patch \
	file://pinctrl/0002-arm-dts-AM33XX-Configure-pinmuxs-for-user-leds-contr.patch \
	file://pinctrl/0003-beaglebone-DT-set-default-triggers-for-LEDS.patch \
	file://pinctrl/0004-beaglebone-add-a-cpu-led-trigger.patch \
	file://cpufreq/0001-arm-dts-AM33XX-Add-device-tree-OPP-table.patch \
	file://adc/0001-input-TSC-ti_tscadc-Correct-register-usage.patch \
	file://adc/0002-input-TSC-ti_tscadc-Add-Step-configuration-as-platfo.patch \
	file://adc/0003-input-TSC-ti_tscadc-set-FIFO0-threshold-Interrupt.patch \
	file://adc/0004-input-TSC-ti_tscadc-Remove-definition-of-End-Of-Inte.patch \
	file://adc/0005-input-TSC-ti_tscadc-Rename-the-existing-touchscreen-.patch \
	file://adc/0006-MFD-ti_tscadc-Add-support-for-TI-s-TSC-ADC-MFDevice.patch \
	file://adc/0007-input-TSC-ti_tsc-Convert-TSC-into-a-MFDevice.patch \
	file://adc/0008-IIO-ADC-tiadc-Add-support-of-TI-s-ADC-driver.patch \
	file://adc/0009-input-ti_am335x_tsc-Make-steps-enable-configurable.patch \
	file://adc/0010-input-ti_am335x_tsc-Order-of-TSC-wires-connect-made-.patch \
	file://adc/0011-input-ti_am335x_tsc-Add-variance-filters.patch \
	file://adc/0012-ti_tscadc-Update-with-IIO-map-interface-deal-with-pa.patch \
	file://adc/0013-ti_tscadc-Match-mfd-sub-devices-to-regmap-interface.patch \
	file://pwm/0001-ARM-OMAP3-hwmod-Corrects-resource-data-for-PWM-devic.patch \
	file://pwm/0002-pwm_backlight-Add-device-tree-support-for-Low-Thresh.patch \
	file://pwm/0003-pwm-pwm-tiecap-Add-device-tree-binding-support-in-AP.patch \
	file://pwm/0004-Control-module-EHRPWM-clk-enabling.patch \
	file://pwm/0005-pwm-pwm-tiecap-Enable-clock-gating.patch \
	file://pwm/0006-PWM-ti-ehrpwm-fix-up-merge-conflict.patch \
	file://pwm/0007-pwm-pwm_test-Driver-support-for-PWM-module-testing.patch \
	file://pwm/0008-arm-dts-DT-support-for-EHRPWM-and-ECAP-device.patch \
	file://pwm/0009-pwm-pwm-tiehrpwm-Add-device-tree-binding-support-EHR.patch \
	file://i2c/0001-pinctrl-pinctrl-single-must-be-initialized-early.patch \
	file://i2c/0002-Bone-DTS-working-i2c2-i2c3-in-the-tree.patch \
	file://i2c/0003-am33xx-Convert-I2C-from-omap-to-am33xx-names.patch \
	file://i2c/0004-beaglebone-fix-backlight-entry-in-DT.patch \
	file://usb/0001-Shut-up-musb.patch \
	file://usb/0002-musb-Fix-crashes-and-other-weirdness.patch \
	file://usb/0003-musb-revert-parts-of-032ec49f.patch \
	file://usb/0004-usb-musb-dsps-get-the-PHY-using-phandle-api.patch \
	file://usb/0005-drivers-usb-otg-add-device-tree-support-to-otg-libra.patch \
	file://usb/0006-usb-otg-nop-add-dt-support.patch \
	file://usb/0007-usb-musb-dsps-add-phy-control-logic-to-glue.patch \
	file://usb/0008-usb-musb-dsps-enable-phy-control-for-am335x.patch \
	file://usb/0009-ARM-am33xx-fix-mem-regions-in-USB-hwmod.patch \
	file://da8xx-fb/0001-omap2-clk-Add-missing-lcdc-clock-definition.patch \
	file://da8xx-fb/0002-da8xx-Allow-use-by-am33xx-based-devices.patch \
	file://da8xx-fb/0003-da8xx-Fix-revision-check-on-the-da8xx-driver.patch \
	file://da8xx-fb/0004-da8xx-De-constify-members-in-the-platform-config.patch \
	file://da8xx-fb/0005-da8xx-Add-standard-panel-definition.patch \
	file://da8xx-fb/0006-da8xx-Add-CDTech_S035Q01-panel-used-by-LCD3-bone-cap.patch \
	file://da8xx-fb/0007-da8xx-fb-add-panel-definition-for-beaglebone-LCD7-ca.patch \
	file://mmc/0001-mmc-omap_hsmmc-Enable-HSPE-bit-for-high-speed-cards.patch \
	file://mmc/0002-am33xx.dtsi-enable-MMC-HSPE-bit-for-all-3-controller.patch \
	file://mmc/0003-omap-hsmmc-Correct-usage-of-of_find_node_by_name.patch \
	file://fixes/0001-ARM-AM33XX-hwmod-Remove-wrong-INIT_NO_RESET-IDLE-fla.patch \
	file://f2fs/0001-f2fs-add-document.patch \
	file://f2fs/0002-f2fs-add-on-disk-layout.patch \
	file://f2fs/0003-f2fs-add-superblock-and-major-in-memory-structure.patch \
	file://f2fs/0004-f2fs-add-super-block-operations.patch \
	file://f2fs/0005-f2fs-add-checkpoint-operations.patch \
	file://f2fs/0006-f2fs-add-node-operations.patch \
	file://f2fs/0007-f2fs-add-segment-operations.patch \
	file://f2fs/0008-f2fs-add-file-operations.patch \
	file://f2fs/0009-f2fs-add-address-space-operations-for-data.patch \
	file://f2fs/0010-f2fs-add-core-inode-operations.patch \
	file://f2fs/0011-f2fs-add-inode-operations-for-special-inodes.patch \
	file://f2fs/0012-f2fs-add-core-directory-operations.patch \
	file://f2fs/0013-f2fs-add-xattr-and-acl-functionalities.patch \
	file://f2fs/0014-f2fs-add-garbage-collection-functions.patch \
	file://f2fs/0015-f2fs-add-recovery-routines-for-roll-forward.patch \
	file://f2fs/0016-f2fs-update-Kconfig-and-Makefile.patch \
	file://f2fs/0017-f2fs-gc.h-make-should_do_checkpoint-inline.patch \
	file://f2fs/0018-f2fs-move-statistics-code-into-one-file.patch \
	file://f2fs/0019-f2fs-move-proc-files-to-debugfs.patch \
	file://f2fs/0020-f2fs-compile-fix.patch \
	file://capebus/0001-i2c-EEPROM-Export-memory-accessor.patch \
	file://capebus/0002-omap-Export-omap_hwmod_lookup-omap_device_build-omap.patch \
	file://capebus/0003-gpio-keys-Pinctrl-fy.patch \
	file://capebus/0004-tps65217-Allow-placement-elsewhere-than-parent-mfd-d.patch \
	file://capebus/0005-pwm-export-of_pwm_request.patch \
	file://capebus/0006-i2c-Export-capability-to-probe-devices.patch \
	file://capebus/0007-pwm-backlight-Pinctrl-fy.patch \
	file://capebus/0008-spi-Export-OF-interfaces-for-capebus-use.patch \
	file://capebus/0009-w1-gpio-Pinctrl-fy.patch \
	file://capebus/0010-w1-gpio-Simplify-get-rid-of-defines.patch \
	file://capebus/0011-arm-dt-Enable-DT-proc-updates.patch \
	file://capebus/0012-ARM-CUSTOM-Build-a-uImage-with-dtb-already-appended.patch \
	file://capebus/0013-beaglebone-create-a-shared-dtsi-for-beaglebone-based.patch \
	file://capebus/0014-beaglebone-enable-emmc-for-bonelt.patch \
	file://capebus/0015-capebus-Core-capebus-support.patch \
	file://capebus/0016-capebus-Add-beaglebone-board-support.patch \
	file://capebus/0017-capebus-Beaglebone-generic-board.patch \
	file://capebus/0018-capebus-Add-beaglebone-geiger-cape.patch \
	file://capebus/0019-capebus-Beaglebone-capebus-DT-update.patch \
	file://capebus/0020-beaglebone-Update-default-config-for-capebus.patch \
	file://defconfig \
"
