#!/bin/sh

grep -B 100000 "modules inserted" pom.xml.template > pom.xml

for pom in */pom.xml ; do
	module=$(echo $pom|sed s,/pom.xml,,)
	echo "<module>$module</module>" >> pom.xml
done
grep -A 100000 "modules inserted" pom.xml.template >> pom.xml
