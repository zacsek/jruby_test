#!/usr/bin/env bash
if [ -z $1 ]
then
	echo "No argument supplied. Expecting one of: [ rb, groovy, scala, yaml, atom ]"
	exit 1
fi

mvn io.takari.polyglot:polyglot-translate-plugin:translate -Dinput=../pom.xml -Doutput=../pom.$1
