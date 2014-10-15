#!/bin/bash
set -e
mvn -B -T 2.0C --fail-at-end install -DskipTests=true
