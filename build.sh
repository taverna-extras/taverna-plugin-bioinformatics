#!/bin/bash
set -e
mvn clean
mvn dependency:go-offline
mvn install -DskipTests=true
mvn test
