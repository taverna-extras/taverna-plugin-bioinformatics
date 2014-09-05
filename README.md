taverna-build
=============

Taverna 3 complete checkout and build script.

This project is a convenience script for checking out all of the code needed to
build Taverna 3 from source - restricted to the repositories at 
https://github.com/taverna/

Note that as each module declares dependencies independently, and many of the intra-dependencies
between Taverna modules are not in SNAPSHOT version, earlier versions might be downloaded
from the myGrid Maven repository during the build process.



## Requirements

 * Linux or equivalent UNIX
 * Open JDK 7 or Oracle Java JDK 7
 * Maven 3
 * GNU make


## Usage

    make
    
    
