package com.gliesereum.share.common.exception.messages;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class LanguageExceptionMessage {

    private LanguageExceptionMessage() {
    }

    public static final ExceptionMessage PACKAGE_WITH_MODULE_AND_ISO_KEY_EXIST = new ExceptionMessage(2000, 400, "Package with same module name and iso key exist");
    public static final ExceptionMessage PACKAGE_BY_ID_FOR_CREATE_FROM_NOT_EXIST = new ExceptionMessage(2001, 400, "Package by id for createFrom not exist");
}
