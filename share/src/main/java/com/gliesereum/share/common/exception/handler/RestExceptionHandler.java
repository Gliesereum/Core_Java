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
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.MediaExceptionMessage.MAX_UPLOAD_SIZE_EXCEEDED;

/**
 * @author yvlasiuk
 * @version 1.0
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
        ErrorResponse errorResponse = new ErrorResponse(VALIDATION_ERROR);
        errorResponse.setPath(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath());
        errorResponse.setTimestamp(LocalDateTime.now());
        addBindingInfo(errorResponse, ex.getBindingResult());
        return buildResponse(errorResponse, VALIDATION_ERROR.getHttpCode(), ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(INVALID_REQUEST_PARAMS);
        errorResponse.setPath(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath());
        errorResponse.setTimestamp(LocalDateTime.now());
        return buildResponse(errorResponse, INVALID_REQUEST_PARAMS.getHttpCode(), ex);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(METHOD_NOT_SUPPORTED);
        errorResponse.setPath(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath());
        errorResponse.setTimestamp(LocalDateTime.now());
        return buildResponse(errorResponse, METHOD_NOT_SUPPORTED.getHttpCode(), ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(BODY_INVALID);
        errorResponse.setPath(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath());
        errorResponse.setTimestamp(LocalDateTime.now());
        return buildResponse(errorResponse, BODY_INVALID.getHttpCode(), ex);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(MAX_UPLOAD_SIZE_EXCEEDED);
        errorResponse.setPath(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setAdditional(ex.getCause().getCause().getMessage());
        return buildResponse(errorResponse, MAX_UPLOAD_SIZE_EXCEEDED.getHttpCode(), ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUndefined(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(UNKNOWN_SERVER_EXCEPTION);
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath());
        errorResponse.setTimestamp(LocalDateTime.now());
        return buildResponse(errorResponse, UNKNOWN_SERVER_EXCEPTION.getHttpCode(), ex);
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
