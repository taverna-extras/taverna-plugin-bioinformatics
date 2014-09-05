taverna-build
=============

Taverna 3 complete checkout and build script.

This project is a convenience script for checking out all of the code needed to
build Taverna 3 from source - restricted to the repositories at 
https://github.com/taverna/

Note that as each module declares dependencies independently, and many of the intra-dependencies
between Taverna modules are not in SNAPSHOT version, earlier versions might be downloaded
from the myGrid Maven repository during the build process.

Running the `Makeile` will perform the following commands:
 * `./update-repos.sh` - retrieve the updated list of [Taverna GitHub repositories](https://github.com/taverna/)
   * ..except those in `repos.ignored`
   * Note: Github API is rate limited to 60 requests pr hour pr IP - the above makes 2 API requests
 * `./clone.sh` - Check out repositories with `git clone` (5 parallel checkouts at a time)
 * `./pull.sh` - `git checkout master` and `git pull` any changes for each repository
 * `./update-pom.sh` - generate the master `pom.xml` from `pom.xml.template`
 * `./build.sh` - Build projects using Maven
   * mvn validate - ensure all Maven plugins are installed and `target/` folders empty
   * mvn dependency:go-offline - Ensure all dependencies can be downloaded
   * mvn install - Compile and install to `~/.m2/repository`
   * mvn test - Run all unit tests

 

## Requirements

 * Linux or equivalent UNIX
 * Open JDK 7 or Oracle Java JDK 7
 * Maven 3
 * GNU make


## Usage

On first usage, run simply:

    make

Alternatively, run the shell commands listed above one by one (including `pull.sh`)    

### Git clone with SSH (read-write)

To check out using SSH instead of HTTP, first check that your [SSH key is registered with github](https://help.github.com/articles/generating-ssh-keys):

  stain@biggie-mint ~/src/taverna-build $ ssh git@github.com
  PTY allocation request failed on channel 0
  Hi stain! You've successfully authenticated, but GitHub does not provide shell access.
  Connection to github.com closed.


Then check out with

    make SSH=true

### Example run

The first run will take quite a while because all repositories will be checked
out, and all their dependencies downloaded and built.


The output should look something like this:

  stain@biggie-mint ~/src $ git clone git@github.com:taverna/taverna-build.git
  Cloning into 'taverna-build'...
  remote: Counting objects: 83, done.
  remote: Compressing objects: 100% (40/40), done.
  remote: Total 83 (delta 42), reused 79 (delta 40)
  Receiving objects: 100% (83/83), 9.82 KiB | 0 bytes/s, done.
  Resolving deltas: 100% (42/42), done.
  Checking connectivity... done.

  stain@biggie-mint ~/src $ cd taverna-build/
  stain@biggie-mint ~/src/taverna-build $ make
  ./update-repos.sh

  [1/2]: https://api.github.com/orgs/taverna/repos?per_page=100&page=1 --> <stdout>
    % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                  Dload  Upload   Total   Spent    Left  Speed
  100  515k  100  515k    0     0   327k      0  0:00:01  0:00:01 --:--:--  327k

  [2/2]: https://api.github.com/orgs/taverna/repos?per_page=100&page=2 --> <stdout>
  100     5  100     5    0     0     41      0 --:--:-- --:--:-- --:--:--     0
  ./clone.sh
  Using base https://github.com/taverna/ - to change use ./clone.sh --ssh or export SSH=true
  Cloning into 'taverna3-osgi-launcher'...
  Cloning into 'taverna3-command-line'...
  Cloning into 'taverna3-commons'...
  Cloning into 'taverna3-command-line-tests'...
  Cloning into 'taverna3-maven-plugin'...
  Receiving objects: 100% (1227/1227), 148.20 KiB | 0 bytes/s, done.
  Resolving deltas: 100% (435/435), done.4.46 MiB | 717.00 KiB/s   
  ...
  ./pull.sh
  ~/src/taverna-build/taverna-databundle ~/src/taverna-build
  Already on 'master'
  ~/src/taverna-build/taverna-dataflow-activity ~/src/taverna-build
  Switched to a new branch 'master'
  ..
  Waiting for git pull to complete..
  ./update-pom.sh
  ./build.sh
  [INFO] Scanning for projects...
  ...
  [INFO] BUILD SUCCESS
  ..
  Downloading: http://repository.mygrid.org.uk/artifactory/repo/org/apache/maven/maven-project/2.2.0/maven-project-2.2.0.jar
  ..

### Updating

To download any changes to repositories and rebuild Taverna code:

    make


To retrieve any new repositories in Github, try instead:

    make all



### Cleaning

To clean the build (delete all `target/` folders), try:

    make clean


To force a full new checkout and build, **deleting any changes**, try instead:

    make deep-clean all

    
