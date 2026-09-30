# Multi-stage Dockerfile for Spring Boot with OpenTelemetry support
# Build stage - using Maven with Java 8
FROM maven:3.8.6-eclipse-temurin-8 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests && ls -lh target/

# Runtime stage - using Eclipse Temurin Java 8 (recommended replacement for OpenJDK)
FROM eclipse-temurin:8-jre-alpine
WORKDIR /app

# Download OpenTelemetry Java Agent
ARG OTEL_AGENT_VERSION=1.32.0
RUN apk add --no-cache wget && \
    wget -O /app/opentelemetry-javaagent.jar \
    https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_AGENT_VERSION}/opentelemetry-javaagent.jar && \
    apk del wget

# Copy the built application - use specific JAR name to avoid confusion
COPY --from=build /app/target/book-management-1.0.0.jar app.jar

# Environment variables for OpenTelemetry
ENV OTEL_SERVICE_NAME=book-management-service
ENV OTEL_TRACES_EXPORTER=otlp
ENV OTEL_METRICS_EXPORTER=otlp
ENV OTEL_LOGS_EXPORTER=otlp
ENV OTEL_EXPORTER_OTLP_ENDPOINT=http://otel-collector:4318
ENV OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf
ENV OTEL_RESOURCE_ATTRIBUTES="service.name=book-management-service,service.version=1.0.0,deployment.environment=docker"
ENV OTEL_EXPORTER_OTLP_TIMEOUT=10000
ENV OTEL_BSP_MAX_EXPORT_BATCH_SIZE=512
ENV OTEL_BSP_SCHEDULE_DELAY=5000

EXPOSE 8080

# Run with OpenTelemetry agent
ENTRYPOINT ["java", "-javaagent:/app/opentelemetry-javaagent.jar", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
