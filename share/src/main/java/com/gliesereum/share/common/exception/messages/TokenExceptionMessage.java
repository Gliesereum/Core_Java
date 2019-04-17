package com.gliesereum.share.common.exception.messages;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class TokenExceptionMessage {

    public static final ExceptionMessage ACCESS_REFRESH_EMPTY = new ExceptionMessage(1100, 400, "Access or Refresh token empty");
    public static final ExceptionMessage ACCESS_EMPTY = new ExceptionMessage(1101, 400, "Access token empty");
    public static final ExceptionMessage ACCESS_TOKEN_NOT_FOUND = new ExceptionMessage(1102, 400, "Access token not found ");
    public static final ExceptionMessage PAIR_NOT_VALID = new ExceptionMessage(1103, 400, "The pair of access and refresh token not valid");
    public static final ExceptionMessage ACCESS_TOKEN_EXPIRED = new ExceptionMessage(1104, 400, "Access token is expired");
    public static final ExceptionMessage REFRESH_TOKEN_EXPIRED = new ExceptionMessage(1105, 400, "Refresh token is expired");
    public static final ExceptionMessage REFRESH_TOKEN_NOT_FOUND = new ExceptionMessage(1106, 400, "Refresh token not found ");
    public static final ExceptionMessage REFRESH_EMPTY = new ExceptionMessage(1107, 400, "Refresh token empty");

}
