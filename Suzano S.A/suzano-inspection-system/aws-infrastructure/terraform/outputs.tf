# Terraform Outputs

# VPC Information
output "vpc_id" {
  description = "ID of the VPC"
  value       = aws_vpc.main.id
}

output "vpc_cidr_block" {
  description = "CIDR block of the VPC"
  value       = aws_vpc.main.cidr_block
}

output "public_subnet_ids" {
  description = "IDs of the public subnets"
  value       = aws_subnet.public[*].id
}

output "private_subnet_ids" {
  description = "IDs of the private subnets"
  value       = aws_subnet.private[*].id
}

# Load Balancer Information
output "alb_dns_name" {
  description = "DNS name of the load balancer"
  value       = aws_lb.main.dns_name
}

output "alb_zone_id" {
  description = "Zone ID of the load balancer"
  value       = aws_lb.main.zone_id
}

output "alb_arn" {
  description = "ARN of the load balancer"
  value       = aws_lb.main.arn
}

# Application URL
output "application_url" {
  description = "URL of the application"
  value       = var.domain_name != "" ? "https://${var.domain_name}" : "https://${aws_lb.main.dns_name}"
}

# Database Information
output "rds_endpoint" {
  description = "RDS instance endpoint"
  value       = aws_db_instance.main.endpoint
  sensitive   = true
}

output "rds_port" {
  description = "RDS instance port"
  value       = aws_db_instance.main.port
}

output "database_name" {
  description = "Database name"
  value       = aws_db_instance.main.db_name
}

# S3 Bucket Information
output "media_files_bucket_name" {
  description = "Name of the S3 bucket for media files"
  value       = aws_s3_bucket.media_files.bucket
}

output "media_files_bucket_arn" {
  description = "ARN of the S3 bucket for media files"
  value       = aws_s3_bucket.media_files.arn
}

output "cloudfront_distribution_id" {
  description = "CloudFront distribution ID"
  value       = aws_cloudfront_distribution.media_files.id
}

output "cloudfront_domain_name" {
  description = "CloudFront distribution domain name"
  value       = aws_cloudfront_distribution.media_files.domain_name
}

# ECS Information
output "ecs_cluster_name" {
  description = "Name of the ECS cluster"
  value       = aws_ecs_cluster.main.name
}

output "ecs_service_name" {
  description = "Name of the ECS service"
  value       = aws_ecs_service.app.name
}

output "ecr_repository_url" {
  description = "URL of the ECR repository"
  value       = aws_ecr_repository.app.repository_url
}

# Security Information
output "db_secret_arn" {
  description = "ARN of the database password secret"
  value       = aws_secretsmanager_secret.db_password.arn
  sensitive   = true
}

# Monitoring Information
output "cloudwatch_dashboard_url" {
  description = "URL of the CloudWatch dashboard"
  value       = "https://${var.aws_region}.console.aws.amazon.com/cloudwatch/home?region=${var.aws_region}#dashboards:name=${aws_cloudwatch_dashboard.main.dashboard_name}"
}

output "sns_topic_arn" {
  description = "ARN of the SNS topic for alerts"
  value       = aws_sns_topic.alerts.arn
}

# WAF Information
output "waf_web_acl_arn" {
  description = "ARN of the WAF Web ACL"
  value       = var.enable_waf ? aws_wafv2_web_acl.main[0].arn : null
}

# Backup Information
output "backup_bucket_name" {
  description = "Name of the S3 bucket for backups"
  value       = aws_s3_bucket.backups.bucket
}

# Network Information
output "nat_gateway_ips" {
  description = "Public IPs of the NAT gateways"
  value       = aws_eip.nat[*].public_ip
}

# Certificate Information
output "ssl_certificate_arn" {
  description = "ARN of the SSL certificate"
  value       = var.ssl_certificate_arn != "" ? var.ssl_certificate_arn : (var.domain_name != "" ? aws_acm_certificate.main[0].arn : null)
}

# Environment Information
output "environment" {
  description = "Environment name"
  value       = var.environment
}

output "project_name" {
  description = "Project name"
  value       = var.project_name
}

output "aws_region" {
  description = "AWS region"
  value       = var.aws_region
}

# Connection Information for Applications
output "database_connection_info" {
  description = "Database connection information"
  value = {
    host     = aws_db_instance.main.address
    port     = aws_db_instance.main.port
    database = aws_db_instance.main.db_name
    username = aws_db_instance.main.username
  }
  sensitive = true
}

output "application_environment_variables" {
  description = "Environment variables for the application"
  value = {
    AWS_REGION                = var.aws_region
    S3_BUCKET_NAME           = aws_s3_bucket.media_files.bucket
    CLOUDFRONT_DOMAIN        = aws_cloudfront_distribution.media_files.domain_name
    DB_SECRET_ARN            = aws_secretsmanager_secret.db_password.arn
    SPRING_PROFILES_ACTIVE   = "aws"
  }
}

# Deployment Information
output "deployment_commands" {
  description = "Commands for deploying the application"
  value = {
    docker_login = "aws ecr get-login-password --region ${var.aws_region} | docker login --username AWS --password-stdin ${aws_ecr_repository.app.repository_url}"
    docker_build = "docker build -t ${aws_ecr_repository.app.name} ."
    docker_tag   = "docker tag ${aws_ecr_repository.app.name}:latest ${aws_ecr_repository.app.repository_url}:latest"
    docker_push  = "docker push ${aws_ecr_repository.app.repository_url}:latest"
    ecs_update   = "aws ecs update-service --cluster ${aws_ecs_cluster.main.name} --service ${aws_ecs_service.app.name} --force-new-deployment --region ${var.aws_region}"
  }
}

