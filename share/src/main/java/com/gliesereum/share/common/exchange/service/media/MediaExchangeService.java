package com.gliesereum.share.common.exchange.service.media;

import com.gliesereum.share.common.model.dto.file.UserFileDto;

import java.io.File;

/**
 * @author vitalij
 * @version 1.0
 */
public interface MediaExchangeService {

     UserFileDto uploadFile(File file);

     void deleteFile(String path);

}
