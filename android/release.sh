#!/bin/bash

REL_DIR=$HOME/src/java/bbct/release
BIN_REL=$REL_DIR/bin/android
SRC_REL=$REL_DIR/src/android

PREFIX=bbct-android

build_apk() {
    local edition=$1
    local version=$2
    local apk_prefix=${PREFIX}-${edition}

    cd ${edition}/main
    ant clean release
    cp bin/${apk_prefix}-release.apk ${BIN_REL}/${edition}/${apk_prefix}-$version.apk
    cd -
}

if [ $# == 2 ]
then {
    lite_version=$1
    premium_version=$2

    # Build APKs
    build_apk lite ${lite_version}
    build_apk premium ${premium_version}

    # Package Lite Edition source
    git archive --format=zip --prefix=bbct/ -o ${SRC_REL}/bbct-lite-src.${lite_version}.zip master lite common
    git archive --format=tar --prefix=bbct/ master lite common | gzip > ${SRC_REL}/bbct-lite-src.${lite_version}.tar.gz
}
else {
    echo Usage: './release <lite_version> <premium_version>'
}
fi
