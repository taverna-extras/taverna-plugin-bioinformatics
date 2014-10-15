#!/bin/bash
set -e
rm -rf licenses/
mkdir -p licenses
# Workaround http://stackoverflow.com/questions/12924132/configure-maven-license-plugin-to-use-excludedgroups-correctly
mvn -T4.0C generate-sources '-Dlicense.excludedGroups=(net.sf.taverna|uk.org.taverna|org.purl.wf4ever.provtaverna).*'

cd licenses/
#cat $(find .. -wholename */license/THIRD-PARTY.txt) | 
cat ../target/generated-sources/license/THIRD-PARTY.txt | \
  grep -v "^Lists of" | sort | uniq > THIRD-PARTY.txt

cat THIRD-PARTY.txt | grep -i 'apache' > apache.txt
cat THIRD-PARTY.txt | grep -vi 'apache' > nonapache.txt

cat nonapache.txt | grep -i 'GNU' > gnu.txt
cat nonapache.txt | grep 'BSD' > bsd.txt
cat nonapache.txt | grep 'MIT' > mit.txt
cat nonapache.txt | grep -vi 'GNU' | grep -v 'BSD' | grep -v 'MIT'> other.txt

cat nonapache.txt | grep 'Unknown license' > unknown.txt
cat nonapache.txt | grep -v 'Unknown license' > other.txt
