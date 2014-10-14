#!/bin/bash
set -e
mvn -B --fail-at-end install -DskipTests=true
