# Demo Instructions

## Quick Start
1. Push code to GitHub repository
2. Configure Jenkins with this Jenkinsfile
3. Run pipeline to see security vulnerabilities detected

## Key Demo Points

### Vulnerabilities Included:
- **SQL Injection** in UserController.java
- **Hardcoded credentials** in application.properties
- **Vulnerable Log4j dependency** in pom.xml
- **Insecure Docker configuration** (running as root)

### Security Tools Results:
- **SonarQube**: Will detect code quality issues and security hotspots
- **OWASP**: Will flag vulnerable Log4j dependency
- **Trivy**: Will find Docker image vulnerabilities
- **Pipeline**: Continues despite vulnerabilities (marked as UNSTABLE)

## Jenkins Configuration Needed:
- Maven 3.8.1
- JDK 11
- SonarQube plugin
- Docker plugin
- SSH credentials for EC2
