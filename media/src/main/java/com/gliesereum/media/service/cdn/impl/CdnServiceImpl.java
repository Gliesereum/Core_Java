package com.gliesereum.media.service.cdn.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.gliesereum.media.config.cdn.CdnProperties;
import com.gliesereum.media.service.cdn.CdnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.MessageFormat;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class CdnServiceImpl implements CdnService {

    private static final String FILE_LOCATION = "{0}/{1}";
    private static final String FILE_URL = "https://{0}.{1}/{2}";

    @Autowired
    private AmazonS3 client;

    @Autowired
    private CdnProperties cdnProperties;

    @Override
    public String uploadFile(String filename, File file) {
        String fileLocation = MessageFormat.format(FILE_LOCATION, cdnProperties.getFolder(), filename);
        PutObjectResult result = client.putObject(
                new PutObjectRequest(cdnProperties.getBucket(), fileLocation, file)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
        return MessageFormat.format(FILE_URL, cdnProperties.getBucket(), cdnProperties.getHost(), fileLocation);

    }
}
