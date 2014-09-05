#!/bin/bash
set -e


if [ "$1" == "--ssh" ] ; then  
	(for a in $(cat repos) ; do echo git@github.com:taverna/$a.git ; done) | xargs --max-procs=5 --max-args=1 git clone
else
	(for a in $(cat repos) ; do echo https://github.com/taverna/$a.git ; done) | xargs --max-procs=5 --max-args=1 git clone
fi


