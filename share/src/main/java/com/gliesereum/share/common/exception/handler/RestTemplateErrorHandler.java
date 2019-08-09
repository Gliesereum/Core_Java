package com.gliesereum.share.common.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gliesereum.share.common.exception.messages.CommonExceptionMessage;
import com.gliesereum.share.common.exception.response.ErrorResponse;
import com.gliesereum.share.common.exception.rest.RestRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class RestTemplateErrorHandler extends DefaultResponseErrorHandler {

    @Override
    protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
        String body = FileCopyUtils.copyToString(new InputStreamReader(response.getBody()));
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse errorResponse = null;
        try {
            errorResponse = mapper.readValue(body, ErrorResponse.class);
        } catch (Exception e) {
        } finally {
            RestRequestException exception = null;
            if (errorResponse != null) {
                exception = new RestRequestException(errorResponse, statusCode.value());
            } else {
                exception = new RestRequestException(LocalDateTime.now(), body, CommonExceptionMessage.UNKNOWN_SERVER_EXCEPTION.getErrorCode(), statusCode.value());
            }
            throw exception;
        }
    }
}
