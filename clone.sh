#!/bin/bash
set -e

if [ "$1" == "--ssh" ] || [ $SSH ] ; then  
  base="git@github.com:taverna/"
else
  base="https://github.com/taverna/"
fi
echo "Using base $base - to change use ./clone.sh --ssh or export SSH=true"


(for a in $(cat repos) ; do if [ ! -d $a ] ; then echo $base$a.git ; fi; done) | xargs -r --max-procs=5 --max-args=1 git clone


