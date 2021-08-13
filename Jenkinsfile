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
                  // fire-and-forget
                  veracode applicationName: 'Verademo', canFailJob: true, createSandbox: true, criticality: 'Medium', debug: true, waitForScan: true, fileNamePattern: '', replacementPattern: '', sandboxName: 'Jenkins Pipeline', scanExcludesPattern: '', scanIncludesPattern: '', scanName: "Jenkins 8-13-2021-Build-${BUILD_NUMBER}", teams: '', uploadExcludesPattern: '', uploadIncludesPattern: '**/**.war', vid: "${$veracode_id}", vkey: "${$veracode_key}"
              }
          }
      }
    //   stage('Veracode Pipeline Scan') {
    //       steps {
    //         sh 'curl -O https://downloads.veracode.com/securityscan/pipeline-scan-LATEST.zip'
    //         sh 'unzip pipeline-scan-LATEST.zip pipeline-scan.jar'
    //         sh 'java -jar pipeline-scan.jar \
    //             --veracode_api_id "${VERACODE_API_ID}" \
    //             --veracode_api_key "${VERACODE_API_SECRET}" \
    //             --file "build/libs/sample.jar" \
    //             --fail_on_severity="Very High, High" \
    //             --fail_on_cwe="80" \
    //             --baseline_file "${CI_BASELINE_PATH}" \
    //             --timeout "${CI_TIMEOUT}" \
    //             --project_name "${env.JOB_NAME}" \
    //             --project_url "${env.GIT_URL}" \
    //             --project_ref "${env.GIT_COMMIT}"'
    //         }
    //   }
   }
}