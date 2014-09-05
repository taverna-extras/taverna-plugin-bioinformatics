#!/bin/bash
set -e
mvn validate
mvn dependency:go-offline
mvn install -DskipTests=true
