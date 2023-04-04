pipeline {
  agent { docker { image 'ace-minimal:12.0.8.0-alpine' } }
  parameters {
    /* These values would be better moved to a configuration file and provided by */
    /* the Config File Provider plugin (or equivalent), but this is good enough   */
    /* for a demo of ACE pipelines that isn't intended as a Jenkins tutorial.     */
    string(name: 'integrationNodeHost',   defaultValue: '10.0.0.2', description: 'Integration node REST API host or IP address')
    string(name: 'integrationNodePort',   defaultValue: '4414', description: 'Integration node REST API port')
    string(name: 'integrationServerName',   defaultValue: 'default', description: 'Integration server name')
  }
  environment {
    LICENSE = 'accept'
  }

  stages {
    stage('Build and UT') {
      steps {
        sh  '''#!/bin/bash
            # Set HOME to somewhere writable by Junit
            export HOME=/tmp

            # Clean up just in case files have been left around
            rm -f */junit-reports/TEST*.xml
            rm -rf $PWD/ace-server

            ./build-and-ut.sh
            '''

      }
      post {
        always {
            junit '**/junit-reports/TEST*.xml'
        }
      }
    }

    stage('EndToEnd Tests') {
      steps {
        sh  '''#!/bin/bash
            # Set HOME to somewhere writable by Junit
            export HOME=/tmp

            # Clean up just in case files have been left around
            rm -f */junit-reports/TEST*.xml
            rm -rf $PWD/ace-server

            ./build-and-run-end-to-end-tests.sh
            '''

      }
      post {
        always {
            junit '**/junit-reports/TEST*.xml'
        }
      }
    }

    stage('Next stage BAR build') {
      steps {
         sh  '''#!/bin/bash
            # Build a single BAR file that contains everything rather than deploying multiple BAR files.
            # Deploying multiple BAR files (for the shared libraries and the application) would work,
	    # but would take longer on redeploys due to reloading the application on each deploy.
            #
            # Tekton pipelines don't have this issue because the application and library are unpacked
	    # into a work directory in a container image in that pipeline, so there is no deploy to a
	    # running server.
	    export SHLIBS=`find */library.descriptor -exec dirname {} ";" | xargs -n1 -i{} echo -y {} | xargs echo`
	    echo "Including libraries: $SHLIBS"
            mqsipackagebar -w $PWD -a demo-application-combined.bar -k App1 $SHLIBS

            # Optional compile for XMLNSC, DFDL, and map resources. Useful as long as the target 
            # broker is the same OS, CPU, and installation including ifixes as the build system.
            # mqsibar --bar-file demo-application-combined.bar --compile
            '''
      }
    }

    stage('Next stage deploy') {
      steps {
        sh "bash -c \"mqsideploy -i ${params.integrationNodeHost} -p ${params.integrationNodePort} -e ${params.integrationServerName} -a demo-application-combined.bar\""
      }
    }

  }
}
