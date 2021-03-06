DESCRIPTION = "tool to display regulator, sensor and clock information"
SUMMARY = "PowerDebug is a tool to display regulator, sensor and clock \
information. Data is refreshed every few seconds. There is also dump option \
to display the information just once."
LICENSE = "EPL-1.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=cdd7b8fa70e88be67e325baae3b8ee95"
DEPENDS = "ncurses"

MMYY = "13.09"
RELEASE = "20${MMYY}"

SRC_URI = "http://releases.linaro.org/${MMYY}/components/power-management/powerdebug/linaro-powerdebug-${PV}-${RELEASE}.tar.bz2"

SRC_URI[md5sum] = "39ca9b9d28b12672b2b98940aea80267"
SRC_URI[sha256sum] = "541eb0507c4868a10ed86bde334bc94f37f891cd2bab97326633c7ddb301407d"

S = "${WORKDIR}/powerdebug-${PV}-${RELEASE}"

do_install () {
    install -D -p -m0755 powerdebug ${D}/${sbindir}/powerdebug
    install -D -p -m0644 powerdebug.8 ${D}${mandir}/man8/powerdebug.8
}
