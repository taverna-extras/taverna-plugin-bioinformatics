#!/bin/bash
set -e
rm -rf licenses/
mkdir -p licenses
cd licenses/

cat $(find .. -wholename */license/THIRD-PARTY.txt) | grep -v "^Lists of" | sort | uniq > THIRD-PARTY.txt
cat THIRD-PARTY.txt | grep -v net.sf.taverna | grep -v 'scufl2' | grep -v 'mygrid' | grep -v 'uk.org.taverna' > external.txt

cat external.txt | grep -i 'apache' > apache.txt
cat external.txt | grep -vi 'apache' > nonapache.txt

cat nonapache.txt | grep -i 'GNU' > gnu.txt
cat nonapache.txt | grep 'BSD' > bsd.txt
cat nonapache.txt | grep 'MIT' > mit.txt
cat nonapache.txt | grep -vi 'GNU' | grep -v 'BSD' | grep -v 'MIT'> other.txt

cat nonapache.txt | grep 'Unknown license' > unknown.txt
cat nonapache.txt | grep -v 'Unknown license' > other.txt
