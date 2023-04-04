# Jenkins pipeline

Used to build the application (including the libraries) via Jenkins and then deploy them to an 
existing integration node. Currently relies on docker on Unix platforms.

![Pipeline overview](ace-submodule-jenkins-app-build.png)

## Running Jenkins

Jenkins can be run from a command line (using Java11) as follows once downloaded:
```
java -jar jenkins.war --httpPort=8080
```
See https://www.jenkins.io/doc/pipeline/tour/getting-started/ for details and download locations. Note
that the docker plugins must be installed for the pipelin ein this repo to build successfully.

## Getting started with the pipeline

This repo relies on having the `ace-minimal` docker container image already built, using
files and instructions from https://github.com/trevor-dolby-at-ibm-com/ace-docker/tree/main/experimental
to achieve this.

The pipeline can be constructed from the main repositories without forking them, but it may
be a good idea to change "GitHub API usage" under "Configure System" in the Jenkins settings
as otherwise messages such as the following may appear regularly:
```
17:07:37 Jenkins-Imposed API Limiter: Current quota for Github API usage has 52 remaining (1 over budget). Next quota of 60 in 58 min. Sleeping for 4 min 9 sec.
17:07:37 Jenkins is attempting to evenly distribute GitHub API requests. To configure a different rate limiting strategy, such as having Jenkins restrict GitHub API requests only when near or above the GitHub rate limit, go to "GitHub API usage" under "Configure System" in the Jenkins settings.
```

To create the pipeline (and following the Jenkins pipeline tour instructions), a "multibranch 
pipeline" should be created and pointed at the github repo.



 It will automatically discover 
the Jenkinsfile in the root directory, allowing the pipeline to be run. The location of the 
integration node should be changed when running the pipeline, with the following parameters
set to appropriate values:

- integrationNodeHost
- integrationNodePort
- integrationServerName

This should start the pipeline, which will then pull the source down, compile and test it, and 
then deploy it to the integration node.

Once the pipeline has completed successfully, the application can be tested by using a browser
or curl to access the application API endpoint at http://localhost:7800/tea/index/1 (assuming a
node without MQ on the default HTTP per-server listener port), which is likely to return null
values unless there is data in the database already:
```
C:\>curl http://localhost:7080/httpFlow
<NS1:libOneExampleOne xmlns:NS1="http://ace.submodule.schemalib.level1/"><NS2:libTwoExampleOneValueOne xmlns:NS2="http://ace.submodule.schemalib.level2/">appValueHTTP</NS2:libTwoExampleOneValueOne></NS1:libOneExampleOne>
```

## Possible enhancements

The pipeline could use a configuration file to contain the location of the integration node, which is
currently configured using parameters for the pipeline itself.
