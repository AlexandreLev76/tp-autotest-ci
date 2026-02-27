pipeline {
    agent any

    tools {
        maven 'Maven'
        nodejs 'Node'
    }

    stages {

        stage('Backend - Tests') {
            steps {
                dir('Backend') {
                    sh 'mvn clean test'
                }
            }
        }


        
    }
}

