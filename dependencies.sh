#!/bin/bash
set -e
mvn -B -T 4.0C --quiet validate
mvn -B -T 4.0C --quiet dependency:go-offline 
