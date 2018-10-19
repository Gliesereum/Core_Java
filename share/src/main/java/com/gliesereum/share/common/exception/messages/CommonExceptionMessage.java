package com.gliesereum.share.common.exception.messages;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 12/10/2018
 */
public class CommonExceptionMessage {

    public static final ExceptionMessage ID_NOT_SPECIFIED = new ExceptionMessage(1000, 400, "Id not specified");
    public static final ExceptionMessage UNKNOWN_SERVER_EXCEPTION = new ExceptionMessage(9000, 500, "Server error");
}
