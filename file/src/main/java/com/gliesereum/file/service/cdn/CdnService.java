package com.gliesereum.file.service.cdn;

import com.amazonaws.services.s3.model.ObjectListing;
import org.springframework.core.io.Resource;

import java.io.File;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface CdnService {

    String uploadFile(String filename, File file, Boolean open);

    Resource loadFile(String filename);

    ObjectListing getAllFiles();

    void changeAccessToFile(String filename, Boolean open);

    void delete(String filename);

    void deleteByKey(String key);
}
