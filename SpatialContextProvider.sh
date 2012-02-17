#!/bin/sh

echo 'For uDPWS is necessary to enable multicast on loopback interface by "ifconfig lo multicast"!' >&2
. $(dirname $0)/class-executor.sh $*
