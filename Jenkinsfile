pipeline {
   agent any
   environment {
      vid = credentials('veracode_id')
      vkey = credentials('veracode_key')
   }
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
              withCredentials([usernamePassword(credentialsId: 'veracode-credentials', passwordVariable: '$key', usernameVariable: '$vid')]) {
                  // fire-and-forget
                  veracode applicationName: 'Verademo', canFailJob: true, createSandbox: true, criticality: 'Medium', debug: true, waitForScan: true, fileNamePattern: '', replacementPattern: '', sandboxName: 'Jenkins Pipeline', scanExcludesPattern: '', scanIncludesPattern: '', scanName: "${BUILD_TAG}", teams: '', uploadExcludesPattern: '', uploadIncludesPattern: '**/**.war', vid: '$vid', vkey: '$vkey'
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
