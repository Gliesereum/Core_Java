package com.gliesereum.share.common.exception.handler;

import com.gliesereum.share.common.exception.CustomException;
import com.gliesereum.share.common.exception.client.AdditionalClientException;
import com.gliesereum.share.common.exception.response.ErrorResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
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

    @ExceptionHandler(AdditionalClientException.class)
    public ResponseEntity<ErrorResponse> handleAdditionalClientException(AdditionalClientException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ex.getErrorCode());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setHttpCode(ex.getHttpCode());
        errorResponse.setPath(ex.getOriginalPath());
        errorResponse.setAdditional(ex.getAdditional());
        return buildResponse(errorResponse, ex);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ex.getErrorCode());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setHttpCode(ex.getHttpCode());
        errorResponse.setPath(ex.getOriginalPath());
        return buildResponse(errorResponse, ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(VALIDATION_ERROR);
        addBindingInfo(errorResponse, ex.getBindingResult());
        return buildResponse(errorResponse, ex);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(VALIDATION_ERROR);
        errorResponse.setAdditional(ex.getMessage());
        return buildResponse(errorResponse, ex);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        return buildResponse(new ErrorResponse(INVALID_REQUEST_PARAMS), ex);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return buildResponse(new ErrorResponse(METHOD_NOT_SUPPORTED), ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        return buildResponse(new ErrorResponse(BODY_INVALID), ex);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(MAX_UPLOAD_SIZE_EXCEEDED);
        errorResponse.setAdditional(ex.getCause().getCause().getMessage());
        return buildResponse(errorResponse, ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUndefined(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(UNKNOWN_SERVER_EXCEPTION);
        errorResponse.setMessage(ex.getMessage());
        return buildResponse(errorResponse, ex);
    }

    private ResponseEntity<ErrorResponse> buildResponse(ErrorResponse response, Exception ex) {
        if (StringUtils.isBlank(response.getPath())) {
            response.setPath(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath());
        }
        response.setTimestamp(LocalDateTime.now());
        HttpStatus httpStatus = HttpStatus.valueOf(response.getHttpCode());
        if (httpStatus.is5xxServerError()) {
            LOG.error("{} error - errorCode: {}, message: {}, path: {}", response.getHttpCode(), response.getCode(), response.getMessage(), response.getPath(), ex);
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
