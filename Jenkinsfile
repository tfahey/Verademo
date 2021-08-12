pipeline {
   agent any

   stages {
      stage('Verify Branch') {
         steps {
            echo "$GIT_BRANCH"
         }
      }
      stage('Package') {
         steps {
            echo "package"
            sh (script: """
                cd app
                mvn package
                cd ..
                pwd
            """)  
            }
      }
      stage('Veracode Pipeline Scan') {
          steps {
              withCredentials([usernamePassword(credentialsId: 'veracode-credentials', passwordVariable: '$veracode_key', usernameVariable: '$veracode_id')]) {
                  veracode applicationName: 'Verademo', canFailJob: true, createSandbox: true, criticality: 'Medium', debug: true, waitForScan: true, fileNamePattern: '', replacementPattern: '', sandboxName: 'Jenkins Pipeline', scanExcludesPattern: '', scanIncludesPattern: '', scanName: 'Jenkins 8-12-2021.1', teams: '', uploadExcludesPattern: '', uploadIncludesPattern: 'target/verademo.war', vid: "${$veracode_id}", vkey: "${$veracode_key}"
              }
          }
      }
   }
}