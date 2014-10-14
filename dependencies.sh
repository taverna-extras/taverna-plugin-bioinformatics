#!/bin/bash
set -e
mvn validate
mvn dependency:go-offline
