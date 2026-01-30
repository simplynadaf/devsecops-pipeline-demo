# Use newer, more secure base image
FROM eclipse-temurin:11-jre-jammy

# Create non-root user for better security
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Create app directory
WORKDIR /app

# Copy application jar
COPY target/devsecops-webapp-1.0.0.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
