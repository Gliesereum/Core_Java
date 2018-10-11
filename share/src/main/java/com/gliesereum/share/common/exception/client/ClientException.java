package com.gliesereum.share.common.exception.client;

import com.gliesereum.share.common.exception.messages.ExceptionMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 11/10/2018
 */

@Getter
@Setter
public class ClientException extends RuntimeException {

    private int errorCode;

    private int httpCode;

    private String message;

    public ClientException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.getMessage());
        this.errorCode = exceptionMessage.getErrorCode();
        this.httpCode = exceptionMessage.getHttpCode();
        this.message = exceptionMessage.getMessage();
    }

    public ClientException(ExceptionMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage.getMessage(), cause);
        this.errorCode = exceptionMessage.getErrorCode();
        this.httpCode = exceptionMessage.getHttpCode();
        this.message = exceptionMessage.getMessage();
    }

    public ClientException(String message, int errorCode, int httpCode) {
        super(message);
        this.errorCode = errorCode;
        this.httpCode = httpCode;
        this.message = message;
    }

    public ClientException(String message, int errorCode, int httpCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpCode = httpCode;
        this.message = message;
    }
}
