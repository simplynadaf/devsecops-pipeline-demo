# Use vulnerable base image for demo
FROM openjdk:11-jre-slim

# Running as root - security issue
USER root

# Install additional packages
RUN apt-get update && apt-get install -y curl wget

# Create app directory
WORKDIR /app

# Copy application jar
COPY target/vulnerable-webapp-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/api/health || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
