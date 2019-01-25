package com.gliesereum.share.common.exception.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class ExceptionMessage {

    private int errorCode;

    private int httpCode;

    private String message;
}
