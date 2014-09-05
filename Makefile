clone:
	./clone.sh

pull: clone
	./pull.sh

pom.xml: pull
	./update-pom.sh

clean:
	mvn clean

deep-clean:
	rm -f pom.xml
	cat repos | xargs rm -rf

build: pom.xml
	./build.sh


all: build
