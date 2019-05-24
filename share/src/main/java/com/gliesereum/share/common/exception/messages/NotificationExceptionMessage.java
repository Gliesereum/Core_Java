package com.gliesereum.share.common.exception.messages;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class NotificationExceptionMessage {

    public static final ExceptionMessage REGISTRATION_TOKEN_EXIST = new ExceptionMessage(1800, 400, "Registration token exist");
    public static final ExceptionMessage REGISTRATION_TOKEN_NOT_EXIST = new ExceptionMessage(1801, 400, "Registration not token exist");

}
