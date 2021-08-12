pipeline {
   agent any

   stages {
      stage('Verify Branch') {
         steps {
            echo "$GIT_BRANCH"
         }
      }
      stage('Docker Build') {
         steps {
            sh(script: '/usr/local/bin/docker images -a')
         }
      }
      stage('Build') {
         steps {
            echo "build"
            sh 'gradle build'  
            }
        }
   }
}