all: build

repos: repos.ignored
	./update-repos.sh

clone: repos
	./clone.sh

pull: clone
	./pull.sh

pom.xml: pull
	./update-pom.sh

clean: pom.xml
	mvn clean

deep-clean: repos
	rm -f pom.xml
	cat repos | xargs rm -rf

build: pom.xml
	./build.sh

