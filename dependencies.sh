#!/bin/bash
set -e
mvn -B -T 4.0C --quiet validate
mvn -B --quiet dependency:go-offline 
