DESCRIPTION = "JMobile Portable"
LICENSE = "Proprietary"

LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=ce29dbb849109f28c0a0358e8fedbc64"

PR = "r152"

SRC_URI = "\
        file://jmobile.desktop \
"

do_fetch() {
	wget http://download.exorembedded.net:8080/Public/JMobile/jmobile2.01_portable_alterakit_cds3.tar.gz -O ${WORKDIR}/jmobile_alterakit.tar.gz
}

do_configure() {
	tar xzf ${WORKDIR}/jmobile_alterakit.tar.gz portable/LICENSE.txt --strip 1 -C ${S}
}

do_install() {
	install -d ${D}${datadir}/applications
	install -m 0644 ${WORKDIR}/jmobile.desktop ${D}${datadir}/applications/
	install -d ${D}/home/root
	install -m 0644 ${WORKDIR}/jmobile_alterakit.tar.gz ${D}/home/root 
}

FILES_${PN} = "${datadir}/applications/ /home/root/jmobile_alterakit.tar.gz"
