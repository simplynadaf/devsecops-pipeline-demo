# 🔐 DevSecOps Pipeline Demo

> 🚀 **Complete DevSecOps pipeline demonstration with automated security scanning and deployment**

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/simplynadaf/devsecops-pipeline-demo)
[![Security](https://img.shields.io/badge/security-scanned-blue)](https://github.com/simplynadaf/devsecops-pipeline-demo)
[![Docker](https://img.shields.io/badge/docker-ready-blue)](https://hub.docker.com/r/sarvar04/devsecop-demo)

**📅 Last Updated**: January 30, 2026 - SSH push working perfectly ✅

## 🏗️ Architecture Flow

```
📱 GitHub → 🔧 Jenkins → 📊 SonarQube → 📦 Maven → 🛡️ OWASP → 🐳 Docker → 🔍 Trivy → ☁️ AWS EC2
```

## 🧩 Components

| Component | Technology | Purpose |
|-----------|------------|---------|
| 💻 **Source Code** | Java Spring Boot | Web application with intentional vulnerabilities |
| 🔨 **Build Tool** | Maven 3.8.1 | Dependency management & compilation |
| 🔍 **Security Scanning** | SonarQube, OWASP, Trivy | Multi-layer security analysis |
| 🐳 **Containerization** | Docker | Secure application packaging |
| 🔄 **CI/CD** | Jenkins | Automated pipeline orchestration |
| ☁️ **Deployment** | AWS EC2 | Cloud infrastructure |

## 🚀 Pipeline Stages

| Stage | Tool | Status | Description |
|-------|------|--------|-------------|
| 1️⃣ | 📥 **Checkout** | ✅ | Source code retrieval from GitHub |
| 2️⃣ | 🔨 **Maven Build** | ✅ | Compile & package application |
| 3️⃣ | 🧪 **Unit Tests** | ✅ | Run 6 comprehensive tests |
| 4️⃣ | 📊 **SonarQube Analysis** | ✅ | Code quality & security hotspots |
| 5️⃣ | 🚪 **Quality Gate** | ⚠️ | Evaluate analysis results |
| 6️⃣ | 🛡️ **OWASP Dependency Check** | ⚠️ | Vulnerability scanning |
| 7️⃣ | 🐳 **Docker Build** | ✅ | Container image creation |
| 8️⃣ | 🔍 **Trivy Security Scan** | ⚠️ | Container security analysis |
| 9️⃣ | 🚨 **Security Gate** | 👤 | Manual security review |
| 🔟 | ✋ **Manual Approval** | 👤 | Production deployment approval |
| 1️⃣1️⃣ | 🚀 **Deploy to EC2** | ✅ | Automated cloud deployment |

## ⚡ Quick Start

### 🔧 Prerequisites
- ☕ Java 11 JDK
- 📦 Maven 3.8.1
- 🐳 Docker
- 🔧 Jenkins
- ☁️ AWS CLI (configured)
- 📱 Git & GitHub account

### 🚀 Setup Instructions
1. 🔌 **Configure Jenkins** with required plugins
2. 🏃 **Start SonarQube** server (`docker run -d -p 9000:9000 sonarqube:9.9-community`)
3. 🔑 **Configure AWS credentials** in Jenkins
4. 📝 **Update EC2 instance details** in Jenkinsfile
5. ▶️ **Run the pipeline** and watch the magic happen!

## 🎯 Demo Features

### 🔴 Intentional Vulnerabilities (for demonstration)
- 💉 **XSS Vulnerability** - Missing output encoding in comment endpoint
- 🔓 **Weak Input Validation** - Insufficient user input sanitization
- 📢 **Information Disclosure** - Debug endpoint exposing sensitive data
- 📦 **Vulnerable Dependencies** - commons-io 2.6, snakeyaml 1.26
- 🔑 **Configuration Issues** - Externalized but demo credentials

### 🛡️ Security Scanning Results
- 📊 **SonarQube**: 3-4 security hotspots, 5-8 code smells
- 🛡️ **OWASP**: 12+ vulnerabilities across dependencies
- 🔍 **Trivy**: Container and dependency security issues
- 🚨 **Security Gates**: Manual approval required for deployment

## 🌐 Live Application

Once deployed, access your application at:
- 🌍 **Web Interface**: `http://YOUR-EC2-IP:8080`
- 🔗 **API Endpoints**: `http://YOUR-EC2-IP:8080/api/`
- 📊 **Health Check**: `http://YOUR-EC2-IP:8080/api/health`

## 📊 Security Reports Dashboard

| Report | Access | Purpose |
|--------|--------|---------|
| 📊 **SonarQube** | `http://localhost:9000` | Code quality metrics |
| 🛡️ **OWASP Report** | Jenkins → Build → Reports | Dependency vulnerabilities |
| 🔍 **Trivy Report** | Jenkins → Build → Artifacts | Container security scan |

## 🎯 Professional Demo Points

- ✅ **Automated Security Scanning** at every stage
- ✅ **Quality Gates** preventing vulnerable deployments  
- ✅ **Manual Approval Process** for security oversight
- ✅ **Comprehensive Reporting** with detailed vulnerability analysis
- ✅ **Professional Web Interface** with modern UI/UX
- ✅ **Cloud-Ready Deployment** on AWS infrastructure
- ✅ **Container Security** with non-root user implementation

## 🤝 Contributing

Feel free to fork this project and submit pull requests for improvements!

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">

**🔐 Built with Security in Mind | 🚀 DevSecOps Best Practices | ☁️ Cloud-Native Architecture**

*Made with ❤️ for demonstrating enterprise-grade DevSecOps workflows*

</div>
