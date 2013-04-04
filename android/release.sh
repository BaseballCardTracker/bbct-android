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

hash() {
    local edition=$1
    local prefix=${PREFIX}-${edition}

    cd ${REL_DIR}/bin/android/${edition}
    md5sum *.apk > ${prefix}.md5
    echo Checking MD5...
    md5sum -c ${prefix}.md5
    sha1sum *.apk > ${prefix}.sha1
    echo Checking SHA1...
    sha1sum -c ${prefix}.sha1
    cd -
}

if [ $# == 2 ]
then {
    lite_version=$1
    premium_version=$2

    echo Building APKs...
    build_apk lite ${lite_version}
    build_apk premium ${premium_version}

    echo Archiving source files...
    git archive --format=zip --prefix=bbct/ -o ${SRC_REL}/bbct-lite-src.${lite_version}.zip master lite common
    git archive --format=tar --prefix=bbct/ master lite common | gzip > ${SRC_REL}/bbct-lite-src.${lite_version}.tar.gz

    echo Generating hashes...
    hash lite
    hash premium

    cd ${REL_DIR}/src/android
    md5sum *.tar.gz *.zip > $PREFIX-src.md5
    echo Checking MD5...
    md5sum -c $PREFIX-src.md5
    sha1sum *.tar.gz *.zip > $PREFIX-src.sha1
    echo Checking SHA1...
    sha1sum -c $PREFIX-src.sha1
    cd -
}
else {
    echo Usage: './release <lite_version> <premium_version>'
}
fi
