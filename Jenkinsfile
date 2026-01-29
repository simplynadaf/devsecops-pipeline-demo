pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'vulnerable-webapp'
        DOCKER_TAG = "${BUILD_NUMBER}"
        SONAR_HOST_URL = 'http://sonarqube:9000'
        EC2_HOST = 'your-ec2-instance-ip'
        EC2_USER = 'ubuntu'
    }
    
    tools {
        maven 'Maven-3.8.1'
        jdk 'JDK-11'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code from GitHub...'
                checkout scm
            }
        }
        
        stage('Maven Build') {
            steps {
                echo 'Building application with Maven...'
                sh 'mvn clean compile package -DskipTests'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
        
        stage('Unit Tests') {
            steps {
                echo 'Running unit tests...'
                sh 'mvn test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'
                withSonarQubeEnv('SonarQube') {
                    sh '''
                        mvn sonar:sonar \
                        -Dsonar.projectKey=devsecops-demo \
                        -Dsonar.host.url=${SONAR_HOST_URL} \
                        -Dsonar.login=${SONAR_AUTH_TOKEN}
                    '''
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                echo 'Waiting for SonarQube Quality Gate...'
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: false
                }
            }
        }
        
        stage('OWASP Dependency Check') {
            steps {
                echo 'OWASP Dependency Check - Disabled for now'
                echo 'Will be configured later'
            }
        }
        
        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'
                script {
                    dockerImage = docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                }
            }
        }
        
        stage('Trivy Security Scan') {
            steps {
                echo 'Running Trivy security scan...'
                sh '''
                    docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
                    -v $PWD:/tmp/.cache/ aquasec/trivy:latest image \
                    --exit-code 1 --severity HIGH,CRITICAL \
                    --format json --output /tmp/.cache/trivy-report.json \
                    ${DOCKER_IMAGE}:${DOCKER_TAG} || true
                '''
                
                sh '''
                    docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
                    -v $PWD:/tmp/.cache/ aquasec/trivy:latest image \
                    --exit-code 1 --severity HIGH,CRITICAL \
                    --format table --output /tmp/.cache/trivy-report.txt \
                    ${DOCKER_IMAGE}:${DOCKER_TAG}
                '''
            }
            post {
                always {
                    archiveArtifacts artifacts: 'trivy-report.*', fingerprint: true
                }
            }
        }
        
        stage('Security Gate') {
            steps {
                echo 'Evaluating security scan results...'
                script {
                    def trivyReport = readFile('trivy-report.txt')
                    if (trivyReport.contains('HIGH') || trivyReport.contains('CRITICAL')) {
                        error('CRITICAL/HIGH vulnerabilities found! Pipeline stopped.')
                    }
                }
            }
        }
        
        stage('Manual Approval') {
            steps {
                script {
                    timeout(time: 10, unit: 'MINUTES') {
                        input message: 'Deploy to Production?', 
                              ok: 'Deploy',
                              submitterParameter: 'APPROVER'
                    }
                }
            }
        }
        
        stage('Deploy to EC2') {
            when {
                anyOf {
                    branch 'main'
                    branch 'master'
                }
            }
            steps {
                echo 'Deploying to EC2 instance...'
                script {
                    // Save Docker image as tar
                    sh "docker save ${DOCKER_IMAGE}:${DOCKER_TAG} > ${DOCKER_IMAGE}-${DOCKER_TAG}.tar"
                    
                    // Copy to EC2 and deploy
                    sshagent(['ec2-ssh-key']) {
                        sh '''
                            scp -o StrictHostKeyChecking=no ${DOCKER_IMAGE}-${DOCKER_TAG}.tar ${EC2_USER}@${EC2_HOST}:/tmp/
                            ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} "
                                docker load < /tmp/${DOCKER_IMAGE}-${DOCKER_TAG}.tar
                                docker stop vulnerable-webapp || true
                                docker rm vulnerable-webapp || true
                                docker run -d --name vulnerable-webapp -p 8080:8080 ${DOCKER_IMAGE}:${DOCKER_TAG}
                                rm /tmp/${DOCKER_IMAGE}-${DOCKER_TAG}.tar
                            "
                        '''
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline completed!'
            cleanWs()
        }
        success {
            echo 'Pipeline succeeded!'
            emailext (
                subject: "SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: "Good news! The pipeline succeeded.",
                to: "team@company.com"
            )
        }
        failure {
            echo 'Pipeline failed!'
            emailext (
                subject: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: "Bad news! The pipeline failed.",
                to: "team@company.com"
            )
        }
        unstable {
            echo 'Pipeline unstable - security vulnerabilities found!'
        }
    }
}
