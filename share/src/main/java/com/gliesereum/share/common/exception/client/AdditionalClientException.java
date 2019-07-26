package com.gliesereum.share.common.exception.client;

import com.gliesereum.share.common.exception.CustomException;
import com.gliesereum.share.common.exception.messages.ExceptionMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Getter
@Setter
public class AdditionalClientException extends CustomException {

    private Map<String, Object> additional;

    public AdditionalClientException(ExceptionMessage exceptionMessage, Map<String, Object> additional) {
        super(exceptionMessage);
        this.additional = additional;
    }

    public AdditionalClientException(ExceptionMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

    public AdditionalClientException(String message, int errorCode, int httpCode) {
        super(message, errorCode, httpCode);
    }

    public AdditionalClientException(String message, int errorCode, int httpCode, Throwable cause) {
        super(message, errorCode, httpCode, cause);
    }
}