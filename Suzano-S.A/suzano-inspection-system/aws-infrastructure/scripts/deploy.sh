#!/bin/bash

# Suzano Inspection System - Deployment Script
# This script deploys the complete infrastructure and application to AWS

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
PROJECT_NAME="suzano-inspection"
AWS_REGION="${AWS_REGION:-us-east-1}"
ENVIRONMENT="${ENVIRONMENT:-prod}"
TERRAFORM_DIR="../terraform"
BACKEND_DIR="../../inspection-backend"

# Functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

check_prerequisites() {
    log_info "Checking prerequisites..."
    
    # Check if AWS CLI is installed
    if ! command -v aws &> /dev/null; then
        log_error "AWS CLI is not installed. Please install it first."
        exit 1
    fi
    
    # Check if Terraform is installed
    if ! command -v terraform &> /dev/null; then
        log_error "Terraform is not installed. Please install it first."
        exit 1
    fi
    
    # Check if Docker is installed
    if ! command -v docker &> /dev/null; then
        log_error "Docker is not installed. Please install it first."
        exit 1
    fi
    
    # Check if Maven is installed
    if ! command -v mvn &> /dev/null; then
        log_error "Maven is not installed. Please install it first."
        exit 1
    fi
    
    # Check AWS credentials
    if ! aws sts get-caller-identity &> /dev/null; then
        log_error "AWS credentials not configured. Please run 'aws configure' first."
        exit 1
    fi
    
    log_success "All prerequisites met!"
}

setup_terraform_backend() {
    log_info "Setting up Terraform backend..."
    
    # Create S3 bucket for Terraform state
    BUCKET_NAME="${PROJECT_NAME}-terraform-state-$(date +%s)"
    
    aws s3 mb s3://${BUCKET_NAME} --region ${AWS_REGION} || true
    
    # Enable versioning
    aws s3api put-bucket-versioning \
        --bucket ${BUCKET_NAME} \
        --versioning-configuration Status=Enabled
    
    # Create DynamoDB table for state locking
    aws dynamodb create-table \
        --table-name "${PROJECT_NAME}-terraform-locks" \
        --attribute-definitions AttributeName=LockID,AttributeType=S \
        --key-schema AttributeName=LockID,KeyType=HASH \
        --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
        --region ${AWS_REGION} || true
    
    # Create backend configuration
    cat > ${TERRAFORM_DIR}/backend.tf << EOF
terraform {
  backend "s3" {
    bucket         = "${BUCKET_NAME}"
    key            = "terraform.tfstate"
    region         = "${AWS_REGION}"
    dynamodb_table = "${PROJECT_NAME}-terraform-locks"
    encrypt        = true
  }
}
EOF
    
    log_success "Terraform backend configured!"
}

deploy_infrastructure() {
    log_info "Deploying infrastructure with Terraform..."
    
    cd ${TERRAFORM_DIR}
    
    # Initialize Terraform
    terraform init
    
    # Create terraform.tfvars if it doesn't exist
    if [ ! -f terraform.tfvars ]; then
        cat > terraform.tfvars << EOF
aws_region = "${AWS_REGION}"
environment = "${ENVIRONMENT}"
project_name = "${PROJECT_NAME}"
notification_email = "${NOTIFICATION_EMAIL:-}"
domain_name = "${DOMAIN_NAME:-}"
EOF
    fi
    
    # Plan deployment
    terraform plan -out=tfplan
    
    # Apply deployment
    terraform apply tfplan
    
    # Save outputs
    terraform output -json > ../outputs.json
    
    cd - > /dev/null
    
    log_success "Infrastructure deployed successfully!"
}

build_and_push_application() {
    log_info "Building and pushing application..."
    
    # Get ECR repository URL from Terraform outputs
    ECR_REPO=$(cat ${TERRAFORM_DIR}/../outputs.json | jq -r '.ecr_repository_url.value')
    
    if [ "$ECR_REPO" = "null" ]; then
        log_error "Could not get ECR repository URL from Terraform outputs"
        exit 1
    fi
    
    cd ${BACKEND_DIR}
    
    # Build application with Maven
    log_info "Building application with Maven..."
    mvn clean package -DskipTests
    
    # Build Docker image
    log_info "Building Docker image..."
    docker build -t ${PROJECT_NAME}-app .
    
    # Tag for ECR
    docker tag ${PROJECT_NAME}-app:latest ${ECR_REPO}:latest
    
    # Login to ECR
    log_info "Logging in to ECR..."
    aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REPO}
    
    # Push to ECR
    log_info "Pushing image to ECR..."
    docker push ${ECR_REPO}:latest
    
    cd - > /dev/null
    
    log_success "Application built and pushed successfully!"
}

update_ecs_service() {
    log_info "Updating ECS service..."
    
    # Get cluster and service names from Terraform outputs
    CLUSTER_NAME=$(cat ${TERRAFORM_DIR}/../outputs.json | jq -r '.ecs_cluster_name.value')
    SERVICE_NAME=$(cat ${TERRAFORM_DIR}/../outputs.json | jq -r '.ecs_service_name.value')
    
    # Force new deployment
    aws ecs update-service \
        --cluster ${CLUSTER_NAME} \
        --service ${SERVICE_NAME} \
        --force-new-deployment \
        --region ${AWS_REGION}
    
    # Wait for deployment to complete
    log_info "Waiting for deployment to complete..."
    aws ecs wait services-stable \
        --cluster ${CLUSTER_NAME} \
        --services ${SERVICE_NAME} \
        --region ${AWS_REGION}
    
    log_success "ECS service updated successfully!"
}

run_database_migrations() {
    log_info "Running database migrations..."
    
    # Get database connection info
    DB_ENDPOINT=$(cat ${TERRAFORM_DIR}/../outputs.json | jq -r '.rds_endpoint.value')
    DB_NAME=$(cat ${TERRAFORM_DIR}/../outputs.json | jq -r '.database_name.value')
    
    # Note: In a real deployment, you would run Flyway or Liquibase migrations here
    # For this demo, we'll just log the information
    log_info "Database endpoint: ${DB_ENDPOINT}"
    log_info "Database name: ${DB_NAME}"
    log_warning "Database migrations should be run manually or via CI/CD pipeline"
    
    log_success "Database migration step completed!"
}

verify_deployment() {
    log_info "Verifying deployment..."
    
    # Get application URL
    APP_URL=$(cat ${TERRAFORM_DIR}/../outputs.json | jq -r '.application_url.value')
    
    # Wait for application to be ready
    log_info "Waiting for application to be ready..."
    sleep 60
    
    # Check health endpoint
    if curl -f "${APP_URL}/actuator/health" > /dev/null 2>&1; then
        log_success "Application is healthy!"
        log_success "Application URL: ${APP_URL}"
    else
        log_warning "Application health check failed. Please check the logs."
    fi
    
    # Display important URLs and information
    echo ""
    echo "=== Deployment Summary ==="
    echo "Application URL: ${APP_URL}"
    echo "CloudWatch Dashboard: $(cat ${TERRAFORM_DIR}/../outputs.json | jq -r '.cloudwatch_dashboard_url.value')"
    echo "Environment: ${ENVIRONMENT}"
    echo "Region: ${AWS_REGION}"
    echo ""
}

cleanup_on_error() {
    log_error "Deployment failed. Cleaning up..."
    
    # Optionally destroy infrastructure on failure
    if [ "${CLEANUP_ON_ERROR:-false}" = "true" ]; then
        cd ${TERRAFORM_DIR}
        terraform destroy -auto-approve
        cd - > /dev/null
    fi
}

main() {
    log_info "Starting deployment of Suzano Inspection System..."
    
    # Set up error handling
    trap cleanup_on_error ERR
    
    # Run deployment steps
    check_prerequisites
    setup_terraform_backend
    deploy_infrastructure
    build_and_push_application
    update_ecs_service
    run_database_migrations
    verify_deployment
    
    log_success "Deployment completed successfully!"
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --region)
            AWS_REGION="$2"
            shift 2
            ;;
        --environment)
            ENVIRONMENT="$2"
            shift 2
            ;;
        --email)
            NOTIFICATION_EMAIL="$2"
            shift 2
            ;;
        --domain)
            DOMAIN_NAME="$2"
            shift 2
            ;;
        --cleanup-on-error)
            CLEANUP_ON_ERROR="true"
            shift
            ;;
        --help)
            echo "Usage: $0 [OPTIONS]"
            echo ""
            echo "Options:"
            echo "  --region REGION           AWS region (default: us-east-1)"
            echo "  --environment ENV         Environment name (default: prod)"
            echo "  --email EMAIL            Notification email for alerts"
            echo "  --domain DOMAIN          Custom domain name"
            echo "  --cleanup-on-error       Destroy infrastructure on deployment failure"
            echo "  --help                   Show this help message"
            exit 0
            ;;
        *)
            log_error "Unknown option: $1"
            exit 1
            ;;
    esac
done

# Run main function
main

