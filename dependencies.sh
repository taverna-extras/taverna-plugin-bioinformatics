#!/bin/bash
set -e
mvn -B validate
mvn -B dependency:go-offline
