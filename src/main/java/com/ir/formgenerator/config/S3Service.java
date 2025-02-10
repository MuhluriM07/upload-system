package com.ir.formgenerator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.net.URI;
import java.time.Instant;

@Service
public class S3Service {

    private final S3Client s3Client;
    
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public S3Service(
        @Value("${aws.s3.endpoint}") String endpoint,
        @Value("${aws.s3.accessKey}") String accessKey,
        @Value("${aws.s3.secretKey}") String secretKey,
        @Value("${aws.s3.region}") String region
    ) {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        this.s3Client = S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .endpointOverride(URI.create(endpoint))  // ✅ Use MinIO endpoint instead of AWS S3
            .region(Region.of(region))
            .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build()) // ✅ Enable path-style access for MinIO
            .build();
    }

    // public void uploadFileToS3(String key, File file) {
    //     Instant start = Instant.now();

    //     s3Client.putObject(PutObjectRequest.builder()
    //         .bucket(bucketName)
    //         .key(key)
    //         .build(), RequestBody.fromFile(file));

    //     Instant end = Instant.now(); // End timing
    //     System.out.println("S3 Upload Time: " + (end.toEpochMilli() - start.toEpochMilli()) + " ms");  
    // }

    public void uploadFileToS3(String key, File file) {
        Instant start = Instant.now();
    
        PutObjectResponse response = s3Client.putObject(PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build(), RequestBody.fromFile(file));
    
        Instant end = Instant.now();
    
        if (response.sdkHttpResponse().isSuccessful()) {
            System.out.println("✅ File uploaded successfully to S3.");
        } else {
            System.out.println("❌ S3 Upload Failed: " + response.sdkHttpResponse().statusText().orElse("Unknown error"));
        }
    
        System.out.println("S3 Upload Time: " + (end.toEpochMilli() - start.toEpochMilli()) + " ms");
    }
    
    
    public void downloadFileFromS3(String key, File destination) {
        Instant start = Instant.now();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build();
        s3Client.getObject(getObjectRequest, ResponseTransformer.toFile(destination.toPath()));
        
        Instant end = Instant.now(); // End timing
        System.out.println("S3 Download Time: " + (end.toEpochMilli() - start.toEpochMilli()) + " ms");
     
    }
}
