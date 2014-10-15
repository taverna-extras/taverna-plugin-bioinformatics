#!/bin/bash
set -e
mvn -B --quiet validate
mvn -B --quiet dependency:go-offline 
