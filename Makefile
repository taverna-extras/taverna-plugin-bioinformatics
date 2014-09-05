clone:
	./clone.sh

pull: clone
	./pull.sh

pom.xml: pull
	./update-pom.sh

clean:
	mvn clean

build: pom.xml
	./build.sh


all: build
