package com.easyupload.photos.s3;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.jdbc.config.annotation.EnableRdsInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;


@Configuration
public class S3Configuration {
	private Logger logger = LoggerFactory.getLogger(S3Configuration.class);

	@Value("${aws.access_key_id}")
	private String awsAccessKeyId;

	@Value("${aws.secret_access_key}")
	private String awsSecretKey;
	
	@Value("${s3.region}")
	private String region;

	@Bean
	public AmazonS3 s3client() {
		logger.info("awsAccessKeyId:    " + awsAccessKeyId);
		logger.info("awsSecretKey:    " + awsSecretKey);
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKeyId, awsSecretKey);
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
								.withRegion(Regions.fromName(region))
		                        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
		                        .build();
		
		return s3Client;
	}
	
	@Bean
	public AmazonDynamoDB dynamoDBclient() {
		logger.info("awsAccessKeyId:    " + awsAccessKeyId);
		logger.info("awsSecretKey:    " + awsSecretKey);
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKeyId, awsSecretKey);
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withRegion(region)
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
				.build();
		
		return client;
	}
	
	@Bean
	public AmazonPollyClient pollyClient() {
		logger.info("awsAccessKeyId:    " + awsAccessKeyId);
		logger.info("awsSecretKey:    " + awsSecretKey);
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKeyId, awsSecretKey);
		AmazonPollyClient pollyClient = new AmazonPollyClient(new AWSStaticCredentialsProvider(awsCreds),
									new ClientConfiguration());
		
		return pollyClient;
	}
}
