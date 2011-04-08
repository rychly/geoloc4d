#!/bin/sh

CLASS=$(basename $0 .sh)
DIR=$(dirname $0)
CLASSDIR=${DIR}/../../build/classes
LIBDIR=${DIR}/../../lib/ws4d-jmeds
LIBS=${LIBDIR}/ws4d-java-se-full.jar
PACKAGE=eu.esonia.but.geoloc4d

java -classpath "${CLASSDIR}:${LIBS}" "${PACKAGE}.${CLASS}" $*
