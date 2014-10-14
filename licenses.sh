#!/bin/bash
cat $(find . -wholename */license/THIRD-PARTY.txt) | grep -v "^List of" > THIRD-PARTY.txt
