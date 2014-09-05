#!/bin/bash
set -e
for a in */. ; do 
	pushd $a 
  git checkout master >/dev/null || true
	git pull >/dev/null &
	popd >/dev/null
done
echo Waiting for git pull to complete..
wait
