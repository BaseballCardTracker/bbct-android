#!/bin/bash

REL_DIR=$HOME/dev/src/java/bbct/release
BIN_REL=$REL_DIR/bin/android
SRC_REL=$REL_DIR/src/android

PREFIX=bbct-android

build_apk() {
    ./gradlew clean assembleLiteRelease assemblePremiumRelease
}

hash_apk() {
    local edition=$1
    local prefix=${PREFIX}-${edition}

    cd ${REL_DIR}/bin/android/${edition} &&
    md5sum *.apk > ${prefix}.md5 &&
    echo Checking MD5... &&
    md5sum -c ${prefix}.md5 &&
    sha1sum *.apk > ${prefix}.sha1 &&
    echo Checking SHA1... &&
    sha1sum -c ${prefix}.sha1 &&
    cd -
}

pull_devel() {
    echo Pull devel branch... &&
    git stash save 'Stash before building release' &&
    git checkout devel/android &&
    git pull upstream devel/android
}

archive() {
    version=$1

    echo Archiving source files... &&
    git archive --format=zip --prefix=bbct/ -o ${SRC_REL}/bbct-lite-src.${version}.zip master lite common &&
    git archive --format=tar --prefix=bbct/ master lite common | gzip > ${SRC_REL}/bbct-lite-src.${version}.tar.gz
}

checksum() {
    cd ${REL_DIR}/src/android &&
    md5sum *.tar.gz *.zip > $PREFIX-src.md5 &&
    echo Checking MD5... &&
    md5sum -c $PREFIX-src.md5 &&
    sha1sum *.tar.gz *.zip > $PREFIX-src.sha1 &&
    echo Checking SHA1... &&
    sha1sum -c $PREFIX-src.sha1 &&
    cd -
}

merge() {
    echo Merge master... &&
    git checkout master &&
    git merge devel/android
}

tag() {
    version=$1
    echo Tag... &&
    git tag l${version} &&
    git tag p${version}
}

push() {
    version=$1

    git push origin master l${version} p${version} &&
    git push upstream master l${version} p${version}
}

if [ $# == 1 ]
then {
    version=$1

    echo Building APKs... &&
    build_apk &&

    merge &&
    tag ${version} &&
    push ${version}
}
else {
    echo Usage: './release <version>'
}
fi
