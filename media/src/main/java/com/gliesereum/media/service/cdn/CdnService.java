package com.gliesereum.media.service.cdn;

import java.io.File;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-27
 */
public interface CdnService {

    String uploadFile(String filename, File file);
}
