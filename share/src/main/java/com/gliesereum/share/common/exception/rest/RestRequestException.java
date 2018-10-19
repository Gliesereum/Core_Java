package com.gliesereum.share.common.exception.rest;

import com.gliesereum.share.common.exception.CustomException;
import com.gliesereum.share.common.exception.messages.ExceptionMessage;
import com.gliesereum.share.common.exception.response.ErrorResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 16/10/2018
 */

@Getter
@Setter
public class RestRequestException extends CustomException {

    private String path;

    private LocalDateTime timestamp;

    public RestRequestException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public RestRequestException(ExceptionMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

    public RestRequestException(String message, int errorCode, int httpCode) {
        super(message, errorCode, httpCode);
    }

    public RestRequestException(String message, int errorCode, int httpCode, Throwable cause) {
        super(message, errorCode, httpCode, cause);
    }

    public RestRequestException(LocalDateTime timestamp, String body, int errorCode, int httpCode) {
        super(body, errorCode, httpCode);
        this.timestamp = timestamp;
    }

    public RestRequestException(ErrorResponse errorResponse, int httpCode) {
        super(errorResponse.getMessage(), errorResponse.getCode(), httpCode);
        this.path = errorResponse.getPath();
        this.timestamp = errorResponse.getTimestamp();
    }
}
