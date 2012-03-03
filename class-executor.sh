#!/bin/sh

CLASS=$(basename $0 .sh)
DIR=$(dirname $0)
CLASSDIR=${DIR}/target/classes
MAVENREPODIR=~/.m2/repository
LIBS=\
"${MAVENREPODIR}/org/ws4d/java/ws4d-java-se-full/2.0.0-beta5/ws4d-java-se-full-2.0.0-beta5.jar\
:${MAVENREPODIR}/org/json/org.json/2.0/org.json-2.0.jar\
:${MAVENREPODIR}/org/restlet/jse/org.restlet/2.1-RC3/org.restlet-2.1-RC3.jar"
PACKAGE=eu.esonia.but.geoloc4d

if [ "${CLASS}" == "class-executor" ]; then
	echo "To use, create symbolic link or include from ClassName.sh." >&2
	exit 1
fi

VM=oracle-jdk-bin-1.7
JAVA=$(java-config --select-vm=${VM} -J)

exec ${JAVA:-java} -classpath "${CLASSDIR}:${LIBS}" "${PACKAGE}.${CLASS}" $*
