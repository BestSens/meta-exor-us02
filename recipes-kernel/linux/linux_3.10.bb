inherit kernel
require recipes-kernel/linux/linux-yocto.inc

# Override SRC_URI in a bbappend file to point at a different source
# tree if you do not want to build from Linus' tree.
SRC_URI = "git://github.com/ExorEmbedded/linux-us02.git;protocol=git;branch=3.10-LTS;nocheckout=1 \
	file://defconfig"
SRC_URI[md5sum] = "7094df7dedb134fa41ee6679a34de190"
SRCREV = "ebfc71d70b39dc5a88b91aa72df669fc67f838cb"

LINUX_VERSION ?= "3.10"
LINUX_VERSION_EXTENSION ?= "-custom"

PR = "r2"
PV = "${LINUX_VERSION}+git${SRCPV}"

# Override COMPATIBLE_MACHINE to include your machine in a bbappend
# file. Leaving it empty here ensures an early explicit build failure.
COMPATIBLE_MACHINE = "(usom02)"

KERNEL_IMAGETYPE = "zImage"
KERNEL_EXTRA_ARGS = "dtbs"

KERNEL_DEVICETREE_usom02 = "socfpga_cyclone5.dtb"

DTB_OUTPUT = "arch/arm/boot/dts"

do_deploy_append() {
   install -d "${DEPLOYDIR}"
   install -m 0644 "${B}/${KERNEL_OUTPUT}" "${DEPLOYDIR}"
   for DTB in ${KERNEL_DEVICETREE}; do
      install -m 0644 "${B}/${DTB_OUTPUT}/${DTB}" "${DEPLOYDIR}"
   done

   cd "${DEPLOYDIR}"
   mv socfpga_bemos_usom.dtb socfpga.dtb
   tar czvf "us02-kernel.tar.gz" "${KERNEL_IMAGETYPE}" socfpga.dtb socfpga_bemos_v8.dtb

   rm -rf  ${KERNEL_IMAGETYPE}*
   rm -rf *.dtb
}

RDEPENDS_kernel-base = ""
