default: pull test

repos: repos.ignored
	./update-repos.sh

clone: repos
	./clone.sh

pull: clone
	./pull.sh

pom.xml: repos
	./update-pom.sh

clean: pom.xml
	rm -f THIRD-PARTY.txt
	mvn clean

deep-clean: repos
	rm -f pom.xml
	cat repos | xargs rm -rf

build: pom.xml
	./build.sh

license: THIRD-PARTY.txt build
	mvn license:add-third-party
	cat $(find . -name THIRD-PARTY.txt) > THIRD-PARTY.txt
	
test: build
	mvn test

all: repos clone pull pom.xml clean build license test
