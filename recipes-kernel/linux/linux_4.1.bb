FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${KERNEL_VERSION}:"

SRC_URI = "git://github.com/ExorEmbedded/linux-us02.git;protocol=git;branch=4.1-LTS"
SRC_URI[md5sum] = "7094df7dedb134fa41ee6679a34de190"
SRCREV = "0dbc2ab33f82a52314230a0974609262ab131893"

include linux.inc

PR = "r1"

B = "${S}"

do_install_prepend() {
   mkdir -p "${D}/lib/modules/${KERNEL_VERSION}"
   touch "${D}/lib/modules/${KERNEL_VERSION}/build"
   mkdir -p "${D}/lib/modules/${KERNEL_VERSION}"
   touch "${D}/lib/modules/${KERNEL_VERSION}/source"
}

INSANE_SKIP_linux += "installed-vs-shipped" 

KERNEL_DEVICETREE = "socfpga_cyclone5_exor.dtb"

KERNEL_VERSION = "4.1"

