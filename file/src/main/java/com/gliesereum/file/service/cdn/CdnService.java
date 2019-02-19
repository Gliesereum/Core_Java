package com.gliesereum.file.service.cdn;

import java.io.File;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface CdnService {

    String uploadFile(String filename, File file);
}
