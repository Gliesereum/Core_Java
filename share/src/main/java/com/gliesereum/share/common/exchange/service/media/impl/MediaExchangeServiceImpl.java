package com.gliesereum.share.common.exchange.service.media.impl;

import com.gliesereum.share.common.exchange.service.media.MediaExchangeService;
import com.gliesereum.share.common.model.dto.media.UserFileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author vitalij
 * @version 1.0
 * @since 2019-01-10
 */
@Service
public class MediaExchangeServiceImpl implements MediaExchangeService {

    private RestTemplate restTemplate;

    private Environment environment;

    private final String UPLOAD_FILE_URL ="exchange.endpoint.media.upload";

    @Autowired
    public MediaExchangeServiceImpl(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = restTemplate;
        this.environment = environment;
    }

    public UserFileDto uploadFile(MultipartFile file){
        String url = environment.getProperty(UPLOAD_FILE_URL);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);
        HttpEntity httpEntity = new HttpEntity<>(file, httpHeaders);
        ResponseEntity<UserFileDto> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, UserFileDto.class);
        UserFileDto result = null;
        if (response.getStatusCode().is2xxSuccessful()) {
            result = response.getBody();
        }
        return result;
    }
}
