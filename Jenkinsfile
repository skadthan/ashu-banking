pipeline {
    agent any
	tools { 
          maven '3.8.4' 
          jdk 'OpenJDK12' 
      }
  /*  parameters {
        string(name: 'MYSQL_ROOT_PASSWORD', defaultValue: 'root', description: 'MySQL password')
        string(name: 'DOCKER_USER', defaultValue: '', description: 'User ID of the Dockerhub')
        string(name: 'DOCKER_TOKEN', defaultValue: '', description: 'Token to upload docker image to dockerhub')
    } 
    */
    stages {
        stage ("Initialize Jenkins Env") {
         steps {
            sh "whoami"
                sh "echo PATH = ${PATH}"
                sh "echo M2_HOME= ${M2_HOME}"
                sh "/usr/local/bin/docker -v"
                sh "docker -v"
         }
        }
        stage('Download Code') {
            steps {
               echo 'checking out'
               checkout scm
            }
        }
        stage('Execute Tests'){
            steps {
                echo 'Testing'
                sh 'mvn test'
            }
        }
        stage('Code Quality Check'){
            steps {
                echo 'Sonar code quality check'
                sh 'mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=admin -Dsonar.password=abcd123'
            }
        }
        stage('Build Application'){
            steps {
                echo 'Building...'
                sh 'mvn clean install -Dmaven.test.skip=true'
            }
        }
        stage('Build and Upload Docker Image') {
            steps {
                echo 'Building Docker image'
                sh 'docker build -t skadthan/ashu-banking:1 .'

                echo 'Uploading Docker image'
                sh '''
                docker tag skadthan/ashu-banking:1 skadthan/ashu-banking:1
                docker login --username=$DOCKER_USER --password=$DOCKER_TOKEN
                docker push skadthan/ashu-banking:1
                '''
            }
        }
       stage('Create Database') {
            steps {
                echo 'Running Database Image'
/* Commented as MySQL is replaced by H2
                sh 'docker stop bankmysql || true && docker rm bankmysql || true'
                sh 'docker run --detach --name=bankmysql --env="MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}" -p 3306:3306 mysql'
                sh 'sleep 20'
                sh 'docker exec -i bankmysql mysql -uroot -p${MYSQL_ROOT_PASSWORD} < src/main/resources/import.sql'
*/
            }
        }
        stage('Deploy and Run') {
            steps {
                echo 'Running Application'
                sh 'docker stop ashu-banking || true && docker rm ashu-banking || true'
            //  Commented as MySQL is replaced by H2
            //  sh 'docker run --detach --name=ashu-banking -p 8800:8800 --link bankmysql:localhost -t skadthan/ashu-banking:1'
                sh 'docker run --detach --name=ashu-banking -p 8800:8800 skadthan/ashu-banking:1'
            }
        }
    }
}
