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
	rm -rf licenses/
	mvn clean

deep-clean: repos
	rm -f pom.xml
	cat repos | xargs rm -rf

dependencies: pom.xml
	./dependencies.sh

licenses: dependencies
	mvn license:add-third-party
	./licenses.sh

build: dependencies
	./build.sh
	
test: build
	mvn test

all: repos clone pull pom.xml clean build licenses test
