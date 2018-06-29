<!--
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
-->

# Bioinformatics plugins for Apache Taverna

Plugins for
[Taverna](http://taverna.incubator.apache.org/) for
supporting bioinformatics-specific services.

This plugin is **no longer maintained** by the Apache Taverna project, but 
has been made available to [taverna-extras](https://github.com/taverna-extras/) 
for archival purposes, open for third-party contributions.

This module may rely on 
[Apache Taverna modules](http://taverna.incubator.apache.org/code) from Maven Central
as well as the older Taverna 2 modules from http://www.mygrid.org.uk/maven/repository - do note that Taverna 2 was licensed as LGPL 2.1 or later.

**Note:** This plugins have **not** been updated for [Apache Taverna 3](https://taverna.incubator.apache.org/download/engine/)


## License

(c) 2007-2014 University of Manchester

(c) 2014-2018 Apache Software Foundation

This product includes software developed at The
[Apache Software Foundation](http://www.apache.org/).

Licensed under the
[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0), see the file
[LICENSE](LICENSE) for details.

The file [NOTICE](NOTICE) contain any additional attributions and
details about embedded third-party libraries and source code.


# Contribute



This plugin is **not actively maintained**, but feel free to raise a
[GitHub pull request](https://github.com/taverna-extras/taverna-plugin-bioinformatics/pulls).

Any contributions received are assumed to be covered by the 
[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0). 


## Prerequisites

* Java 1.7 or newer (tested with OpenJDK 1.8)
* [Apache Maven](https://maven.apache.org/download.html) 3.2.5 or newer (older
  versions probably also work)


# Building

To build, use

    mvn clean install

This will build each module and run their tests.


## Skipping tests

To skip the tests (these can be timeconsuming), use:

    mvn clean install -DskipTests


If you are modifying this source code independent of the
Apache Taverna project, you may not want to run the
[Rat Maven plugin](https://creadur.apache.org/rat/apache-rat-plugin/)
that enforces Apache headers in every source file - to disable it, try:

    mvn clean install -Drat.skip=true
