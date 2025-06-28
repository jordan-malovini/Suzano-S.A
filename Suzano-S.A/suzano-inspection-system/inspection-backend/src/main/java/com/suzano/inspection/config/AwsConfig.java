package com.suzano.inspection.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {

    @Value("${aws.access-key-id:}")
    private String accessKeyId;

    @Value("${aws.secret-access-key:}")
    private String secretAccessKey;

    @Value("${aws.s3.region:us-east-1}")
    private String region;

    @Bean
    public S3Client s3Client() {
        if (accessKeyId.isEmpty() || secretAccessKey.isEmpty()) {
            // Use default credential provider chain (IAM roles, environment variables, etc.)
            return S3Client.builder()
                    .region(Region.of(region))
                    .build();
        } else {
            // Use explicit credentials
            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
            return S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                    .build();
        }
    }
}

