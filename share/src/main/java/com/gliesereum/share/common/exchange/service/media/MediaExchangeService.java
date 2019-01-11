package com.gliesereum.share.common.exchange.service.media;

import com.gliesereum.share.common.model.dto.media.UserFileDto;

import java.io.File;

/**
 * @author vitalij
 * @version 1.0
 * @since 2019-01-10
 */
public interface MediaExchangeService {

     UserFileDto uploadFile(File file);

}
