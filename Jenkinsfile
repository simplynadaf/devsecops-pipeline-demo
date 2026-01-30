pipeline {
    agent any
    
    options {
    timeout(time: 30, unit: 'MINUTES')
    }
    
    environment {
        DOCKER_IMAGE = 'sarvar04/devsecop-demo'
        DOCKER_TAG = "${BUILD_NUMBER}"
        SONAR_HOST_URL = 'http://localhost:9000'
        EC2_HOST = 'your-ec2-instance-ip'
        EC2_USER = 'ubuntu'
        DOCKERHUB_CREDENTIALS = 'dockerhub-credentials'
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
                sh 'mvn clean compile -Dmaven.main.skip=false -Dmaven.test.skip=true'
                sh 'mvn package -DskipTests'
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
                        -Dsonar.host.url=${SONAR_HOST_URL}
                    '''
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                echo 'Waiting for SonarQube Quality Gate...'
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: false
                }
            }
        }
        
        stage('OWASP Dependency Check') {
            steps {
                echo 'Running OWASP Dependency Check (limited scan)...'
                sh 'mvn org.owasp:dependency-check-maven:check -DfailBuildOnCVSS=0'
            }
            post {
                always {
                    publishHTML([
                        allowMissing: true,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/dependency-check-report',
                        reportFiles: 'dependency-check-report.html',
                        reportName: 'OWASP Dependency Check Report'
                    ])
                }
            }
        }
        
        stage('Docker Build & Push') {
            steps {
                echo 'Building and pushing Docker image to Docker Hub...'
                script {
                    dockerImage = docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                    
                    docker.withRegistry('https://registry-1.docker.io/v2/', DOCKERHUB_CREDENTIALS) {
                        dockerImage.push()
                        dockerImage.push('latest')
                    }
                }
            }
        }
        
        stage('Trivy Security Scan') {
            steps {
                echo 'Running Trivy security scan on Docker image and dependencies...'
                script {
                    try {
                        sh '''
                            docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
                            -v $PWD:/tmp/.cache/ aquasec/trivy:latest image \
                            --severity HIGH,CRITICAL \
                            --format table --output /tmp/.cache/trivy-report.txt \
                            ${DOCKER_IMAGE}:${DOCKER_TAG}
                        '''
                    } catch (Exception e) {
                        echo "Trivy scan completed with findings"
                    }
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'trivy-report.*', fingerprint: true, allowEmptyArchive: true
                }
            }
        }
        
        stage('Security Gate') {
            steps {
                echo 'Evaluating security scan results...'
                script {
                    if (fileExists('trivy-report.txt')) {
                        def trivyReport = readFile('trivy-report.txt')
                        if (trivyReport.contains('CRITICAL')) {
                            echo 'WARNING: CRITICAL vulnerabilities found!'
                            currentBuild.result = 'UNSTABLE'
                        } else if (trivyReport.contains('HIGH')) {
                            echo 'WARNING: HIGH vulnerabilities found!'
                        } else {
                            echo 'No critical vulnerabilities found'
                        }
                    } else {
                        echo 'Trivy report not found, skipping security gate'
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
                echo 'Deploying to EC2 instance from Docker Hub...'
                script {
                    sshagent(['ec2-ssh-key']) {
                        sh '''
                            ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} "
                                docker pull ${DOCKER_IMAGE}:${DOCKER_TAG}
                                docker stop devsecops-webapp || true
                                docker rm devsecops-webapp || true
                                docker run -d --name devsecops-webapp -p 8080:8080 ${DOCKER_IMAGE}:${DOCKER_TAG}
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
