#!/bin/sh
# Not exactly compliant JSON parsing here.. also assume no more than 200 repositories
curl -L -H "Accept: application/vnd.github.v3+json" 'https://api.github.com/orgs/taverna/repos?per_page=100&page=[1-2]' | grep \"name\" | cut -d '"' -f 4 > repos
