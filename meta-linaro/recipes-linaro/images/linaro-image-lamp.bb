require linaro-image-common.inc

IMAGE_INSTALL += " \
    apache2 \
    fwts \
    grub \
    mysql5-server \
    mysql5-client \
    mongodb \
    php-fpm \
    php-fpm-apache2 \
    packagegroup-core-buildessential \
    ${SDK_IMAGE_INSTALL}"

IMAGE_FEATURES += "\
	dev-pkgs \
	staticdev-pkgs \
	tools-debug \
	tools-sdk \
	"
