#!/bin/sh

CLASS=$(basename $0 .sh)
DIR=$(dirname $0)
CLASSDIR=${DIR}/target/classes
MAVENREPODIR=~/.m2/repository
LIBS=${MAVENREPODIR}/org/ws4d/java/ws4d-java-se-full/2.0.0-beta4a/ws4d-java-se-full-2.0.0-beta4a.jar
PACKAGE=eu.esonia.but.geoloc4d

java -classpath "${CLASSDIR}:${LIBS}" "${PACKAGE}.${CLASS}" $*
