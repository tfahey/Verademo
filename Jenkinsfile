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
      stage('Native SCA script') {
         steps {
            withCredentials([string(credentialsId: 'SRCCLR_API_TOKEN', variable: 'SRCCLR_API_TOKEN')]) {
            echo "srcclr scan"
               sh (script: """
                  cd app
                  pwd
                  curl -sSL https://download.sourceclear.com/ci.sh | sh
                  cd ..
                  pwd
               """)
            }
         }
      }
      stage('Veracode Upload and Scan') {
          steps {
              withCredentials([usernamePassword(credentialsId: 'veracode-credentials', passwordVariable: '$veracode_key', usernameVariable: '$veracode_id')]) {
                  // fire-and-forget
                  veracode applicationName: 'Verademo', canFailJob: true, createSandbox: true, criticality: 'Medium', debug: true, waitForScan: true, fileNamePattern: '', replacementPattern: '', sandboxName: 'Jenkins Pipeline', scanExcludesPattern: '', scanIncludesPattern: '', scanName: "Jenkins 8-13-2021-Build-${BUILD_NUMBER}", teams: '', uploadExcludesPattern: '', uploadIncludesPattern: '**/**.war', vid: "${$veracode_id}", vkey: "${$veracode_key}"
              }
          }
      }
   }
   post {
      always {
         archiveArtifacts artifacts: 'app/target/*.war', followSymlinks: false
      }
   }
}