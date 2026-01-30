# Demo Vulnerabilities Added for SonarQube Showcase

## 🎯 **Purpose**
These demo classes showcase various security vulnerabilities, code smells, and bugs that SonarQube will detect during analysis.

## 📁 **Files Added**
- `VulnerabilityDemoService.java` - Service with intentional vulnerabilities
- `SecurityDemoController.java` - REST controller with security issues  
- `VulnerabilityDemoServiceTest.java` - Test class with low coverage

## 🔍 **SonarQube Will Detect:**

### 🚨 **CRITICAL Issues (2-3)**
- Hardcoded passwords
- SQL injection vulnerabilities
- Exposed sensitive information

### 🔴 **HIGH Issues (4-5)**
- XSS vulnerabilities
- Path traversal attacks
- Resource leaks (unclosed connections)
- Null pointer exceptions

### 🟡 **MEDIUM Issues (6-8)**
- Weak cryptography (MD5)
- ReDoS (Regular Expression DoS)
- Information disclosure
- Poor error handling

### 🔵 **LOW Issues (3-4)**
- Magic numbers
- Generic exception handling
- Minor information leaks

### 🟢 **CODE SMELLS (10-15)**
- Cognitive complexity
- Too many parameters
- Duplicated code
- Unused variables
- Long parameter lists
- Empty methods

### 📊 **COVERAGE Issues**
- Low test coverage (~30-40%)
- Missing edge case tests
- Commented out tests
- Incomplete test scenarios

## 🎭 **Demo Endpoints**
Access these endpoints to trigger vulnerabilities:
- `/api/demo/unsafe-comment?comment=<script>alert('XSS')</script>`
- `/api/demo/user/1' OR '1'='1`
- `/api/demo/file?filename=../../../etc/passwd`
- `/api/demo/debug` (exposes sensitive data)

## ⚠️ **IMPORTANT**
These are **INTENTIONAL VULNERABILITIES** for demonstration only. 
**DO NOT USE IN PRODUCTION!**

## 📈 **Expected SonarQube Results**
- **Security Hotspots**: 8-12
- **Bugs**: 6-10  
- **Code Smells**: 15-25
- **Coverage**: 30-40%
- **Maintainability Rating**: C or D
- **Reliability Rating**: C or D
- **Security Rating**: D or E
