#!/bin/sh

grep -B 100000 "modules inserted" pom.xml.template > pom.xml

for repo in $(cat repos) ; do
  if [ -f $repo/pom.xml ] ; then
    echo "<module>$repo</module>" >> pom.xml
  fi
done
grep -A 100000 "modules inserted" pom.xml.template >> pom.xml
