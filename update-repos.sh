#!/bin/sh
# Not exactly compliant JSON parsing here.. also assume no more than 200 repositories

URI='https://api.github.com/orgs/taverna/repos?per_page=100&page=[1-2]'

curl -L -H "Accept: application/vnd.github.v3+json" "$URI" | 
  grep \"name\" | cut -d '"' -f 4 | 
  grep -v taverna2- | grep -v taverna-build | sort > repos.github

grep -v -x -f repos.ignored repos.github > repos  
