package com.gliesereum.file.service.cdn.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.gliesereum.file.config.cdn.CdnProperties;
import com.gliesereum.file.service.cdn.CdnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.MessageFormat;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
@Slf4j
public class CdnServiceImpl implements CdnService {

    private static final String FILE_LOCATION = "{0}/{1}";
    private static final String FILE_URL = "https://{0}.{1}/{2}";

    @Autowired
    private AmazonS3 client;

    @Autowired
    private CdnProperties cdnProperties;

    @Override
    public String uploadFile(String filename, File file, Boolean open) {
        String fileLocation = getFileLocation(filename);
        client.putObject(
                new PutObjectRequest(cdnProperties.getBucket(), fileLocation, file)
                        .withCannedAcl(getAccess(open)));

        return MessageFormat.format(FILE_URL, cdnProperties.getBucket(), cdnProperties.getHost(), fileLocation);

    }

    @Override
    public Resource loadFile(String filename) {
        Resource resource = null;
        String fileLocation = getFileLocation(filename);
        S3Object object = client.getObject(cdnProperties.getBucket(), fileLocation);
        S3ObjectInputStream objectContent = object.getObjectContent();
        try {
            resource = new InputStreamResource(objectContent);
        } catch (Exception e) {
            log.error("Error while read file", e);
        }
        return resource;
    }

    @Override
    public ObjectListing getAllFiles() {
        return client.listObjects(cdnProperties.getBucket());
    }

    @Override
    public void changeAccessToFile(String filename, Boolean open) {
        client.setObjectAcl(cdnProperties.getBucket(), getFileLocation(filename), getAccess(open));
    }

    @Override
    public void delete(String filename) {
        deleteByKey(getFileLocation(filename));
    }

    @Override
    public void deleteByKey(String key) {
        client.deleteObject(cdnProperties.getBucket(), key);
    }

    private String getFileLocation(String filename) {
        return MessageFormat.format(FILE_LOCATION, cdnProperties.getFolder(), filename);
    }

    private CannedAccessControlList getAccess(Boolean open) {
        if (open) {
            return CannedAccessControlList.PublicRead;
        } else {
            return CannedAccessControlList.AuthenticatedRead;
        }
    }
}
