# DevSecOps Demo Setup Steps - Fresh Start Guide

## Prerequisites
- Java 11 JDK
- Maven 3.8.1
- Docker
- Jenkins
- AWS CLI (configured)
- Git
- GitHub account

---

## Step 1: GitHub Repository Setup ⏳ TO DO

1. **Create GitHub Repository**:
   - Repository name: `devsecops-pipeline-demo`
   - Visibility: Public or Private
   - Initialize: No README, no .gitignore

2. **Push Local Code to GitHub**:
   ```bash
   cd /home/ubuntu/devsecops-demo
   git init
   git add .
   git commit -m "Initial DevSecOps demo setup"
   git remote add origin https://github.com/YOUR_USERNAME/devsecops-pipeline-demo.git
   git branch -M main
   git push -u origin main
   ```

---

## Step 2: Infrastructure Setup ⏳ TO DO

### 2.1 Launch EC2 Instance (Deployment Target)
- **Instance Type**: t2.micro or t3.small
- **AMI**: Ubuntu 22.04 or Amazon Linux 2
- **Security Group Rules**:
  - SSH (22) - Your IP
  - HTTP (8080) - Anywhere (for demo)
- **Key Pair**: Download and save .pem file
- **Note**: Save the Public IP address

### 2.2 Install Docker on EC2
```bash
ssh -i your-key.pem ubuntu@EC2-PUBLIC-IP

# Install Docker
sudo apt update
sudo apt install -y docker.io
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -aG docker ubuntu

# Logout and login again for docker group to take effect
exit
```

### 2.3 Start SonarQube (Local or Separate Server)
```bash
docker run -d --name sonarqube -p 9000:9000 sonarqube:9.9-community
```
- **Access**: http://localhost:9000
- **Default Login**: admin/admin
- **Action**: Change password on first login

---

## Step 3: Jenkins Configuration ⏳ TO DO

### 3.1 Install Required Jenkins Plugins
**Manage Jenkins → Manage Plugins → Available**
- Maven Integration Plugin
- SonarQube Scanner Plugin
- Docker Pipeline Plugin
- HTML Publisher Plugin
- SSH Agent Plugin
- JUnit Plugin

### 3.2 Configure Global Tools
**Manage Jenkins → Global Tool Configuration**

**Maven Configuration**:
- Name: `Maven-3.8.1`
- Install automatically: ✓
- Version: 3.8.1

**JDK Configuration**:
- Name: `JDK-11`
- Install automatically: ✓
- Version: Java 11

### 3.3 Configure SonarQube Integration
**In SonarQube (http://localhost:9000)**:
1. Login as admin
2. Go to: My Account → Security → Generate Tokens
3. Token Name: `jenkins-token`
4. Type: User Token
5. Click Generate
6. **COPY THE TOKEN** (you won't see it again)

**In Jenkins (Manage Jenkins → Configure System)**:
1. Scroll to **SonarQube servers**
2. Click **Add SonarQube**
3. Name: `SonarQube`
4. Server URL: `http://localhost:9000` (or your SonarQube URL)
5. Server authentication token:
   - Click **Add** → **Jenkins**
   - Kind: Secret text
   - Secret: Paste the SonarQube token
   - ID: `sonar-token`
   - Description: `SonarQube Token`
   - Click **Add**
6. Select `sonar-token` from dropdown
7. Click **Save**

### 3.4 Add Docker Hub Credentials
**Manage Jenkins → Manage Credentials → (global) → Add Credentials**
- Kind: `Username with password`
- ID: `dockerhub-credentials`
- Description: `Docker Hub Credentials`
- Username: Your Docker Hub username
- Password: Your Docker Hub password (or access token)
- Click **Create**

### 3.5 Add EC2 SSH Credentials
**Manage Jenkins → Manage Credentials → (global) → Add Credentials**
- Kind: `SSH Username with private key`
- ID: `ec2-ssh-key`
- Description: `EC2 SSH Key`
- Username: `ubuntu` (or `ec2-user` for Amazon Linux)
- Private Key: Click **Enter directly**
  - Paste your EC2 private key (.pem file content)
- Click **Create**

---

## Step 4: Update Configuration Files ⏳ TO DO

### 4.1 Update Jenkinsfile
**File**: `/home/ubuntu/devsecops-demo/Jenkinsfile`

**Update these lines**:
```groovy
environment {
    DOCKER_IMAGE = 'sarvar04/devsecop-demo'      // ✅ ALREADY SET
    DOCKER_TAG = "${BUILD_NUMBER}"
    SONAR_HOST_URL = 'http://localhost:9000'     // Update if SonarQube is on different server
    EC2_HOST = 'YOUR-EC2-PUBLIC-IP'              // ← UPDATE THIS
    EC2_USER = 'ubuntu'                          // or 'ec2-user' for Amazon Linux
    DOCKERHUB_CREDENTIALS = 'dockerhub-credentials'
}
```

### 4.2 Commit and Push Changes
```bash
cd /home/ubuntu/devsecops-demo
git add Jenkinsfile
git commit -m "Updated EC2 host configuration"
git push origin main
```

---

## Step 5: Create Jenkins Pipeline Job ⏳ TO DO

1. **Jenkins Dashboard** → **New Item**
2. **Item name**: `DevSecOps-Pipeline-Demo`
3. **Type**: Pipeline
4. Click **OK**

**Configure Pipeline**:
- **Description**: DevSecOps demo with security scanning
- **Pipeline Definition**: Pipeline script from SCM
- **SCM**: Git
- **Repository URL**: Your GitHub repository URL
  - Example: `https://github.com/YOUR_USERNAME/devsecops-pipeline-demo.git`
- **Credentials**: None (if public repo) or add GitHub credentials
- **Branch Specifier**: `*/main`
- **Script Path**: `Jenkinsfile`
- Click **Save**

---

## Step 6: Run the Pipeline ⏳ TO DO

1. **Click "Build Now"** in Jenkins
2. **Watch the pipeline execute** through all stages

**Expected Pipeline Flow**:
```
✅ Checkout (from GitHub)
✅ Maven Build (creates JAR)
✅ Unit Tests (runs tests)
✅ SonarQube Analysis (code quality scan)
⚠️ Quality Gate (may show warnings)
✅ OWASP Dependency Check (finds vulnerabilities)
✅ Docker Build (creates container image)
✅ Trivy Security Scan (scans container)
⚠️ Security Gate (evaluates results)
⏸️ Manual Approval (requires human approval)
✅ Deploy to EC2 (if approved)
```

---

## Step 7: Access the Application ⏳ TO DO

**After successful deployment**:
- URL: `http://YOUR-EC2-PUBLIC-IP:8080`
- You'll see: Professional DevSecOps Demo web interface
- API endpoints: `http://YOUR-EC2-PUBLIC-IP:8080/api/`

---

## Step 8: View Security Reports 📊

### SonarQube Dashboard
- URL: `http://localhost:9000`
- Project: `devsecops-demo`
- **Shows**: Bugs, Code Smells, Security Hotspots, Coverage

### OWASP Report (in Jenkins)
- Go to build → **OWASP Dependency Check Report**
- **Shows**: commons-io 2.6, snakeyaml 1.26 vulnerabilities

### Trivy Report (in Jenkins)
- Go to build → **Artifacts** → `trivy-report.txt`
- **Shows**: Container and dependency vulnerabilities

---

## Demo Points for Client Presentation 🎯

1. **GitHub Repository**
   - Show clean code structure
   - Explain Spring Boot application
   - Point out security scanning configuration

2. **Jenkins Pipeline**
   - Show automated stages
   - Explain each security scanning tool
   - Demonstrate quality gates

3. **SonarQube Analysis**
   - Code quality metrics
   - Security hotspots detected
   - Maintainability ratings

4. **OWASP Dependency Check**
   - Vulnerable dependencies found
   - CVE details and severity
   - Remediation recommendations

5. **Trivy Container Scan**
   - Docker image vulnerabilities
   - Base image security issues
   - Dependency vulnerabilities

6. **Security Gates**
   - Pipeline stops on critical issues
   - Manual approval required
   - Prevents vulnerable code deployment

7. **Live Application**
   - Professional web interface
   - Working API endpoints
   - Deployed securely to AWS

---

## Troubleshooting Guide 🔧

### SonarQube Issues
- **Not accessible**: `docker ps` to check if running
- **Can't connect**: Verify URL and port 9000
- **Token error**: Regenerate token in SonarQube

### Jenkins Issues
- **Maven not found**: Check Global Tool Configuration
- **Docker not available**: Install Docker plugin
- **SonarQube connection fails**: Verify token credential

### EC2 Deployment Issues
- **SSH fails**: Check security group allows port 22
- **Key permission error**: `chmod 400 your-key.pem`
- **Docker not found on EC2**: Install Docker on EC2
- **Port 8080 not accessible**: Check security group allows port 8080

### Build Failures
- **Maven build fails**: Check Java 11 is configured
- **Unit tests fail**: Check test dependencies in pom.xml
- **Docker build fails**: Check Dockerfile syntax

---

## Current Project Status 📋

### ✅ Completed
- Project code structure created
- Application renamed to `devsecops-webapp`
- Professional HTML frontend added
- Unit tests created
- Vulnerable dependencies added (commons-io, snakeyaml)
- OWASP configuration optimized
- Docker configuration updated
- All files ready in `/home/ubuntu/devsecops-demo`

### ⏳ To Do Tomorrow
1. Push code to GitHub
2. Launch EC2 instance
3. Configure Jenkins (plugins, tools, credentials)
4. Create Jenkins pipeline job
5. Run first build
6. Demo to client

---

## Quick Start Commands for Tomorrow 🚀

```bash
# 1. Push to GitHub
cd /home/ubuntu/devsecops-demo
git remote add origin https://github.com/YOUR_USERNAME/devsecops-pipeline-demo.git
git push -u origin main

# 2. Start SonarQube (if not running)
docker start sonarqube

# 3. Check SonarQube
curl http://localhost:9000

# 4. SSH to EC2 (after launching)
ssh -i your-key.pem ubuntu@YOUR-EC2-IP

# 5. Test application locally (optional)
mvn clean package
java -jar target/devsecops-webapp-1.0.0.jar
```

---

## Notes 📝
- All code changes are saved locally in `/home/ubuntu/devsecops-demo`
- Application name changed from "vulnerable-webapp" to "devsecops-webapp"
- OWASP configured with safer settings to avoid AWS abuse alerts
- Pipeline includes 11 automated stages + 1 manual approval
- Total setup time: ~30-45 minutes tomorrow

**Ready for fresh start tomorrow! 🎯**
