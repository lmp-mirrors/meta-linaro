SUMMARY = "Linux Test Project"
DESCRIPTION = "The Linux Test Project is a joint project with SGI, IBM, OSDL, and Bull with a goal to deliver test suites to the open source community that validate the reliability, robustness, and stability of Linux. The Linux Test Project is a collection of tools for testing the Linux kernel and related features."
HOMEPAGE = "http://ltp.sourceforge.net"
SECTION = "console/utils"
LICENSE = "GPLv2 & GPLv2+ & LGPLv2+ & LGPLv2.1+ & BSD-2-Clause"
LIC_FILES_CHKSUM = "\
    file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
    file://testcases/kernel/controllers/freezer/COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
    file://testcases/kernel/controllers/freezer/run_freezer.sh;beginline=5;endline=17;md5=86a61d2c042d59836ffb353a21456498 \
    file://testcases/kernel/hotplug/memory_hotplug/COPYING;md5=e04a2e542b2b8629bf9cd2ba29b0fe41 \
    file://testcases/kernel/hotplug/cpu_hotplug/COPYING;md5=e04a2e542b2b8629bf9cd2ba29b0fe41 \
    file://testcases/open_posix_testsuite/COPYING;md5=216e43b72efbe4ed9017cc19c4c68b01 \
    file://testcases/realtime/COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e \
    file://tools/netpipe-2.4/COPYING;md5=9e3781bb5fe787aa80e1f51f5006b6fa \
    file://tools/netpipe-2.4-ipv6/COPYING;md5=9e3781bb5fe787aa80e1f51f5006b6fa \
    file://tools/top-LTP/proc/COPYING;md5=aefc88eb8a41672fbfcfe6b69ab8c49c \
    file://tools/pounder21/COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
    file://utils/benchmark/kernbench-0.42/COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
    file://utils/ffsb-6.0-rc2/COPYING;md5=c46082167a314d785d012a244748d803 \
"

DEPENDS = "attr libaio libcap acl openssl"
SRCREV = "c8b3e28097e7d3208df9daceaf92c25eae87ebf0"

SRC_URI = "git://github.com/linux-test-project/ltp.git \
    file://0001-Rename-runtests_noltp.sh-script-so-have-unique-name.patch \
    file://regen-makefile.patch \
    file://ffsb-arm64.patch \
    file://ltp-arm64.patch \
    file://run-auto-use-default-profile-hrtimer-pro-measurement.patch \
"

S = "${WORKDIR}/git"

inherit autotools

TARGET_CC_ARCH += "${LDFLAGS}"
EXTRA_OECONF = " --with-power-management-testsuite --with-realtime-testsuite "

export prefix = "/opt/ltp"
export exec_prefix = "/opt/ltp"

# ltp doesn't regenerate ffsb-6.0-rc2 configure and hardcode configure call.
# we explicitly force regeneration of that directory and pass configure options.
do_configure_prepend() {
    (cd utils/ffsb-6.0-rc2; autoreconf -fvi; ./configure ${CONFIGUREOPTS})
}

do_install(){
    install -d ${D}/opt/ltp/
    oe_runmake DESTDIR=${D} SKIP_IDCHECK=1 install

    # Copy POSIX test suite into ${D}/opt/ltp/testcases by manual
    cp -r testcases/open_posix_testsuite ${D}/opt/ltp/testcases

    # We need to remove all scripts which depend on /usr/bin/expect, since expect is not supported in oe-core
    # We will add expect for enhancement in future
    find ${D} -type f -print | xargs grep "\!.*\/usr\/bin\/expect" | awk -F":" '{print $1}' | xargs rm -f
}

RDEPENDS_${PN} = "perl e2fsprogs-mke2fs"

FILES_${PN}-dbg += "\
    /opt/ltp/runtest/.debug \
    /opt/ltp/testcases/bin/.debug \
    /opt/ltp/testcases/bin/*/bin/.debug \
    /opt/ltp/testcases/bin/*/test/.debug \
    /opt/ltp/scenario_groups/.debug \
    /opt/ltp/testscripts/.debug \
    /opt/ltp/testscripts/open_posix_testsuite/.debug \
"

FILES_${PN}-staticdev += "/opt/ltp/lib/libmem.a"

FILES_${PN} += "/opt/ltp/* /opt/ltp/runtest/* /opt/ltp/scenario_groups/* /opt/ltp/testcases/bin/* /opt/ltp/testcases/bin/*/bin/* /opt/ltp/testscripts/* /opt/ltp/testcases/open_posix_testsuite/* /opt/ltp/testcases/open_posix_testsuite/conformance/* /opt/ltp/testcases/open_posix_testsuite/Documentation/* /opt/ltp/testcases/open_posix_testsuite/functional/* /opt/ltp/testcases/open_posix_testsuite/include/* /opt/ltp/testcases/open_posix_testsuite/scripts/* /opt/ltp/testcases/open_posix_testsuite/stress/* /opt/ltp/testcases/open_posix_testsuite/tools/*"

# Avoid generated binaries stripping. Otherwise some of the ltp tests such as ldd01 & nm01 fails
INHIBIT_PACKAGE_STRIP = "1"
