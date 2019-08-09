package com.gliesereum.share.common.exception.handler;

import org.springframework.http.HttpStatus;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class RestTemplateOnlyServerErrorHandler extends RestTemplateErrorHandler {

    @Override
    protected boolean hasError(HttpStatus statusCode) {
        return statusCode.is5xxServerError();
    }
}
