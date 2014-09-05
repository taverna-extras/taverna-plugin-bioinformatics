#!/bin/bash
set -e
for a in */. ; do 
	pushd $a 
	git pull
	popd
done
