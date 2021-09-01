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
      stage ('Veracode pipeline scan') {
         steps {
            echo 'Veracode Pipeline scanning'
            withCredentials([ usernamePassword ( 
               credentialsId: 'veracode_login', usernameVariable: 'VERACODE_API_ID', passwordVariable: 'VERACODE_API_KEY') ]) {
                  script {
                     // this try-catch block will show the flaws in the Jenkins log, and yet not
                     // fail the build due to any flaws reported in the pipeline scan
                     // alternately, you could add --fail_on_severity '', but that would not show the
                     // flaws in the Jenkins log
                     // issue_details true: add flaw details to the results.json file
                     try {
                        if(isUnix() == true) {
                           sh """
                              curl https://downloads.veracode.com/securityscan/pipeline-scan-LATEST.zip -o pipeline-scan.zip
                              unzip -u pipeline-scan.zip pipeline-scan.jar
                              java -jar pipeline-scan.jar --veracode_api_id '${VERACODE_API_ID}' --veracode_api_key '${VERACODE_API_KEY}' --file app/target/verademo.war --issue_details true
                           """
                        }
                        else {
                           powershell """
                              curl  https://downloads.veracode.com/securityscan/pipeline-scan-LATEST.zip -o pipeline-scan.zip
                              Expand-Archive -Path pipeline-scan.zip -DestinationPath veracode_scanner
                              java -jar veracode_scanner\\pipeline-scan.jar --veracode_api_id '${VERACODE_API_ID}' \
                                 --veracode_api_key '${VERACODE_API_KEY}' \
                                 --file target/verademo.war --issue_details true
                           """
                        }
                     } catch (err) {
                        echo 'Pipeline err: ' + err
                       }
                     }    
               } 
            echo "Pipeline scan done (failures ignored, results available in ${WORKSPACE}/results.json)"
         }
      }
      stage('Veracode Upload and Scan') {
          steps {
              withCredentials([usernamePassword(credentialsId: 'veracode-credentials', passwordVariable: '$veracode_key', usernameVariable: '$veracode_id')]) {
                  // fire-and-forget
                  veracode applicationName: 'Verademo', canFailJob: true, createSandbox: true, criticality: 'Medium', debug: true, waitForScan: true, fileNamePattern: '', replacementPattern: '', sandboxName: 'Jenkins Pipeline', scanExcludesPattern: '', scanIncludesPattern: '', scanName: "${BUILD_TAG}", teams: '', uploadExcludesPattern: '', uploadIncludesPattern: '**/**.war', vid: "${$veracode_id}", vkey: "${$veracode_key}"
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