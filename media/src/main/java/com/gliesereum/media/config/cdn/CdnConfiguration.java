package com.gliesereum.media.config.cdn;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Configuration
public class CdnConfiguration {

    @Autowired
    private CdnProperties cdnProperties;

    @Bean
    public AmazonS3 client() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(
                cdnProperties.getAccessKey(),
                cdnProperties.getSecretKey());
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(cdnProperties.getHost(), cdnProperties.getRegion()))
                .build();
    }
}
