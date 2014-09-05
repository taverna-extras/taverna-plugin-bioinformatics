default: pull build

repos: repos.ignored
	./update-repos.sh

clone: repos
	./clone.sh

pull: clone
	./pull.sh

pom.xml: repos
	./update-pom.sh

clean: pom.xml
	mvn clean

deep-clean: repos
	rm -f pom.xml
	cat repos | xargs rm -rf

build: pom.xml
	./build.sh

all: repos clone pull build
