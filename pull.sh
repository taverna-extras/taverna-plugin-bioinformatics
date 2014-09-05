#!/bin/bash
set -e
for a in */. ; do 
	pushd $a 
  git checkout master || true
	git pull
	popd
done
