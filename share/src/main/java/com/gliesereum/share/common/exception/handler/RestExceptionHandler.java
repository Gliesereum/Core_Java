package com.gliesereum.share.common.exception.handler;

import com.gliesereum.share.common.exception.CustomException;
import com.gliesereum.share.common.exception.messages.ExceptionMessage;
import com.gliesereum.share.common.exception.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.UNKNOWN_SERVER_EXCEPTION;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 17/10/2018
 */

@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ex.getErrorCode());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath());
        errorResponse.setTimestamp(LocalDateTime.now());

        return buildResponse(errorResponse, ex.getHttpCode(), ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUndefined(Exception ex, WebRequest request) {
        ExceptionMessage unknownServerException = UNKNOWN_SERVER_EXCEPTION;
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(unknownServerException.getErrorCode());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath());
        errorResponse.setTimestamp(LocalDateTime.now());
        return buildResponse(errorResponse, unknownServerException.getHttpCode(), ex);
    }

    private ResponseEntity<ErrorResponse> buildResponse(ErrorResponse response, int statusCode, Exception ex) {
        HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
        if (httpStatus.is5xxServerError()) {
            LOG.error("{} error - errorCode: {}, message: {}, path: {}", statusCode, response.getCode(), response.getMessage(), response.getPath(), ex);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

}
