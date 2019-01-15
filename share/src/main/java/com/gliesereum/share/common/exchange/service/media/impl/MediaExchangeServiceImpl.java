package com.gliesereum.share.common.exchange.service.media.impl;

import com.gliesereum.share.common.exchange.service.media.MediaExchangeService;
import com.gliesereum.share.common.model.dto.media.UserFileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vitalij
 * @version 1.0
 * @since 2019-01-10
 */
@Service
public class MediaExchangeServiceImpl implements MediaExchangeService {

    private RestTemplate restTemplate;

    private Environment environment;

    private final String UPLOAD_FILE_URL = "exchange.endpoint.media.upload";

    private final String DELETE_FILE_URL = "exchange.endpoint.media.delete";

    @Autowired
    public MediaExchangeServiceImpl(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = restTemplate;
        this.environment = environment;
    }

    public UserFileDto uploadFile(File file) {
        String url = environment.getProperty(UPLOAD_FILE_URL);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(file));

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<UserFileDto> response = restTemplate.postForEntity(url, httpEntity, UserFileDto.class);

        UserFileDto result = null;
        if (response.getStatusCode().is2xxSuccessful()) {
            result = response.getBody();
        }
        return result;
    }

    public void deleteFile(String path) {
        String url = environment.getProperty(DELETE_FILE_URL);
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("path", path); //todo add correct variable
        restTemplate.delete(url, urlVariables);
    }
}
