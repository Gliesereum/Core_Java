package com.gliesereum.share.common.exception.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 11/10/2018
 */
@Data
@AllArgsConstructor
public class ExceptionMessage {

    private int errorCode;

    private int httpCode;

    private String message;
}
