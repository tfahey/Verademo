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
                mvn -f app/pom.xml clean package
                pwd
            """)  
            }
      }
      stage('Veracode Upload and Scan') {
          steps {
              withCredentials([usernamePassword(credentialsId: 'veracode-credentials', passwordVariable: '$veracode_key', usernameVariable: '$veracode_id')]) {
                  // fire-and-forget
                  veracode applicationName: 'Verademo', canFailJob: true, createSandbox: true, criticality: 'Medium', debug: true, waitForScan: true, fileNamePattern: '', replacementPattern: '', sandboxName: 'Jenkins Pipeline', scanExcludesPattern: '', scanIncludesPattern: '', scanName: "${BUILD_TAG}", teams: '', uploadExcludesPattern: '', uploadIncludesPattern: '**/**.war', vid: '$veracode_id', vkey: '$veracode_key'
              }
          }
      }
   }
   post {
      always {
         archiveArtifacts artifacts: 'app/target/*.war, results.json', followSymlinks: false
      }
   }
}
