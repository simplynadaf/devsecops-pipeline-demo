# DevSecOps Pipeline Demo

This project demonstrates a complete DevSecOps pipeline with security scanning tools.

**Last Updated**: January 30, 2026

## Architecture
GitHub → Jenkins → SonarQube → Maven → OWASP → Docker → Trivy → EC2

## Components
- **Source Code**: Java Spring Boot web application (with intentional vulnerabilities)
- **Build Tool**: Maven
- **Security Scanning**: SonarQube, OWASP Dependency Check, Trivy
- **Containerization**: Docker
- **CI/CD**: Jenkins
- **Deployment**: AWS EC2

## Pipeline Stages
1. Checkout from GitHub
2. Maven Build
3. SonarQube Analysis
4. OWASP Dependency Check
5. Docker Build
6. Trivy Security Scan
7. Deploy to EC2

## Setup Instructions
1. Configure Jenkins with required plugins
2. Set up SonarQube server
3. Configure AWS credentials in Jenkins
4. Update EC2 instance details in Jenkinsfile
5. Run the pipeline

## Intentional Vulnerabilities (for demo)
- SQL Injection vulnerability
- Hardcoded credentials
- Vulnerable dependencies
- Insecure Docker configuration
