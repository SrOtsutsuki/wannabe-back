pipeline {
    options {
            // Disallow concurrent executions of the Pipeline. Can be useful
            // for preventing simultaneous accesses to shared resources, etc.
            disableConcurrentBuilds()
        }
    agent any
        tools {
            maven 'Maven'
        }
    stages {
        stage('BUILD - TEST - PACKAGE'){
            steps{
                echo 'Buildeando ....'
                sh 'mvn clean package -Dmaven.test.skip'

            }
        }
        stage('Clean docker'){
            steps{
                echo 'stop dockers'
                sh 'docker-compose kill'
                echo 'clean old containers'
                sh 'docker rm wannabe-back --force'
                echo 'clean old images'
                sh 'docker rmi wannabe-back --force'
            }
        }

        stage('Dockerize Wannabe-Back'){
            steps{
                 echo 'Dockerize ....'
                 sh 'docker build . -t wannabe-back'
            }
        }

        stage('Up docker-compose'){
            steps{
                 echo 'Up docker-compose'
                 sh 'docker-compose up -d'

            }
        }

    }

}