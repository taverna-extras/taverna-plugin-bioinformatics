default: pull test

repos: repos.ignored
	./update-repos.sh

clone: repos
	./clone.sh

pull: clone
	./pull.sh

pom.xml: repos pom.xml.template
	./update-pom.sh

clean: pom.xml
	rm -rf licenses/
	mvn -B clean

deep-clean: repos
	rm -f pom.xml
	cat repos | xargs rm -rf

dependencies: pom.xml
	./dependencies.sh

licenses: dependencies
	./licenses.sh

build: dependencies
	./build.sh
	
test: build
	mvn -B test

all: repos clone pull pom.xml clean build licenses test
