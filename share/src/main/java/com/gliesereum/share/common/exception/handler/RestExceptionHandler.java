package com.gliesereum.share.common.exception.handler;

import com.gliesereum.share.common.exception.CustomException;
import com.gliesereum.share.common.exception.messages.ExceptionMessage;
import com.gliesereum.share.common.exception.response.ErrorResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.*;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        ExceptionMessage validationError = VALIDATION_ERROR;
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(validationError.getErrorCode());
        errorResponse.setMessage(validationError.getMessage());
        errorResponse.setPath(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath());
        errorResponse.setTimestamp(LocalDateTime.now());
        addBindingInfo(errorResponse, ex.getBindingResult());
        return buildResponse(errorResponse, validationError.getHttpCode(), ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(BODY_INVALID.getErrorCode());
        errorResponse.setMessage(BODY_INVALID.getMessage());
        errorResponse.setPath(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath());
        errorResponse.setTimestamp(LocalDateTime.now());
        return buildResponse(errorResponse, BODY_INVALID.getHttpCode(), ex);
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

    private void addBindingInfo(ErrorResponse errorResponse, BindingResult bindingResult) {
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        if (CollectionUtils.isNotEmpty(allErrors)) {
            Map<String, String> validationMap = new HashMap<>();
            allErrors.forEach(e -> {
                String errorKey;
                if (e instanceof FieldError) {
                    errorKey = ((FieldError) e).getField();
                } else {
                    errorKey = e.getObjectName();
                }
                validationMap.put(errorKey, e.getDefaultMessage());
            });
            errorResponse.setAdditional(validationMap);
        }
    }

}
